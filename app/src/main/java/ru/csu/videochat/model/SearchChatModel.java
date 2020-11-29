package ru.csu.videochat.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.csu.videochat.R;
import ru.csu.videochat.model.entries.Chat;
import ru.csu.videochat.model.entries.User;
import ru.csu.videochat.model.utilities.CategoryAdapter;
import ru.csu.videochat.model.utilities.Constants;
import ru.csu.videochat.model.utilities.PreferenceManager;
import ru.csu.videochat.network.ApiClient;
import ru.csu.videochat.network.IApiService;

public class SearchChatModel {
    public interface ICompleteCallback {
        void onComplete(User user);
    }

    private Context context;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;

    public static SearchChatModel getInstance(Context context) {
        return new SearchChatModel(context);
    }

    public SearchChatModel(Context context) {
        this.context = context;
        this.database = FirebaseFirestore.getInstance();
        this.preferenceManager = new PreferenceManager(context);
    }

    public void loadChat(String selectedCategory, ICompleteCallback callback) {
        String[] ages = preferenceManager.getStringArray(Constants.KEY_COMPANION_AGES);
        String yourAge = preferenceManager.getString(Constants.KEY_YOUR_AGE);
        Task<QuerySnapshot> task;
        if (ages.length == 0)
            task = database
                    .collection(Constants.KEY_COLLECTION_CHATS)
                    .whereEqualTo(Constants.KEY_CHAT_UID_SECOND_TOKEN, "")
                    .whereEqualTo(Constants.KEY_CATEGORY_ID, selectedCategory)
                    .whereEqualTo(Constants.KEY_AGE_ID, "")
                    .get();
        else {
            task = database
                    .collection(Constants.KEY_COLLECTION_CHATS)
                    .whereEqualTo(Constants.KEY_CHAT_UID_SECOND_TOKEN, "")
                    .whereEqualTo(Constants.KEY_CATEGORY_ID, selectedCategory)
                    .whereEqualTo(yourAge, "true")
                    .whereIn(Constants.KEY_AGE_ID, Arrays.asList(ages))
                    .get();
        }
        task.addOnSuccessListener(querySnapshot -> {
            // Подключаем второго юзера
            Chat chat = null;
            String documentId = null;
            List<DocumentSnapshot> documents = querySnapshot.getDocuments();
            if (documents == null || documents.isEmpty()) {
                // Создаем новый чат
                if (ages.length == 0)
                    addChat(selectedCategory, callback, null);
                else addChat(selectedCategory, callback, ages);
                return;
            }

            for (DocumentSnapshot document : documents) {
                Log.e("Model", document.getData().toString());
                chat = document.toObject(Chat.class);
                documentId = document.getId();

                preferenceManager.putString(Constants.KEY_CHAT_ID, documentId);

                break;
            }
            if (chat != null && documentId != null) {
                updateChat(documentId);
                callback.onComplete(new User(chat.getUid2Token()));
            }
        })
                .addOnFailureListener(exception -> {
                });

    }

    private void addChat(String selectedCategory, ICompleteCallback callback, String[] ages) {
        String fcmToken = MessagingService.getToken(context);
        Chat chat;
        if (ages != null) {
            String yourAge = preferenceManager.getString(Constants.KEY_YOUR_AGE);
            String age17 = "", age21 = "", age25 = "", age30 = "", age31 = "";
            for (String age : ages) {
                if (age.equals("age17")) {
                    age17 = "true";
                    continue;
                }
                if (age.equals("age21")) {
                    age21 = "true";
                    continue;
                }
                if (age.equals("age25")) {
                    age25 = "true";
                    continue;
                }
                if (age.equals("age30")) {
                    age30 = "true";
                    continue;
                }
                if (age.equals("age31")) {
                    age31 = "true";
                }
            }
            chat = new Chat(fcmToken, "", selectedCategory, yourAge,
                    age17, age21, age25, age30, age31);

        } else chat = new Chat(fcmToken, "", selectedCategory, "",
                "", "", "", "", "");


        DocumentReference newChat = database.collection(Constants.KEY_COLLECTION_CHATS).document();
        preferenceManager.putString(Constants.KEY_CHAT_ID, newChat.getId());

        newChat.set(chat)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(error -> {
//                    Log.e("AddChat", error.getMessage());
                });

        listener = newChat.addSnapshotListener((value, error) -> {
            if (error != null) {
//                Log.w("AddChat", "listen:error", error);
                return;
            }

            if (value != null && value.exists()) {
                Map<String, Object> data = value.getData();
                if (data != null) {

                    if (data.get(Constants.KEY_CHAT_UID_SECOND_TOKEN) != "") {
                        Chat currentChat = value.toObject(Chat.class);

                        Log.d("AddChat", "Current user2: " + currentChat.getUid2Token());
                        callback.onComplete(new User(currentChat.getUid2Token()));
                    }
                }
            } else {
                Log.d("AddChat", "Current data: null");
            }
        });

    }

    private ListenerRegistration listener;

    private void updateChat(String documentId) {
        String secondToken = MessagingService.getToken(context);
        if (secondToken == null) {
//            Log.e("Token2", "MessagingService.getToken == null");
            return;
        }

        database.collection(Constants.KEY_COLLECTION_CHATS)
                .document(documentId)
                .update(Constants.KEY_CHAT_UID_SECOND_TOKEN, secondToken)
                .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Собеседник готов присоединиться", Toast.LENGTH_SHORT).show();
                            initiateMeeting(secondToken);
                            if (listener != null)
                                listener.remove();
                        }
                )
                .addOnFailureListener(error ->
                        Toast.makeText(context, "Неправильное подключение: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    public void deleteChat() {
        String documentId = preferenceManager.getString(Constants.KEY_CHAT_ID);
        if (documentId != null)
            database.collection(Constants.KEY_COLLECTION_CHATS)
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnFailureListener(error ->
                            Toast.makeText(context, "Не удалось удалить чат: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                    );
    }

    private void initiateMeeting(String receiverToken) {
        String documentId = preferenceManager.getString(Constants.KEY_CHAT_ID);

        database.collection(Constants.KEY_COLLECTION_CHATS)
                .document(documentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Chat chat = documentSnapshot.toObject(Chat.class);
                    if (chat != null) {
                        String inviterToken = chat.getUid1Token();
                        createMessage(receiverToken, inviterToken);
                    }
                });
    }

    private void createMessage(String inviterToken, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken);

            String meetingRoom = preferenceManager.getString(Constants.KEY_CHAT_ID);
            data.put(Constants.REMOTE_MSG_MEETING_ROOM, meetingRoom);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION);
        } catch (Exception exception) {
            Toast.makeText(context, "Invitation error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {
        ApiClient.getClient().create(IApiService.class)
                .sendRemoteMessage(
                        Constants.getRemoteMessageHeaders(),
                        remoteMessageBody
                )
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                                connectVideo();
                            }
                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                            Log.e("SendRemote", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
    }

    public void connectVideo() {
        try {
            URL server = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions conferenceOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(server)
                            .setWelcomePageEnabled(false)
                            .setRoom(preferenceManager.getString(Constants.KEY_CHAT_ID))
                            .build();
            JitsiMeetActivity.launch(context, conferenceOptions);
        } catch (Exception ignored) {
        }
    }
}
