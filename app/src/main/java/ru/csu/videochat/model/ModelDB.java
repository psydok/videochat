package ru.csu.videochat.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.csu.videochat.model.entries.Chat;
import ru.csu.videochat.model.entries.User;
import ru.csu.videochat.network.ApiClient;
import ru.csu.videochat.network.IApiService;

import static android.content.ContentValues.TAG;

public class ModelDB {
    public interface ICompleteCallback {
        void onComplete(User user);
    }

    private Context context;
    private FirebaseFirestore database;
    private String inviterToken = null;

    public ModelDB(Context context, @NonNull String inviterToken) {
        this.context = context;
        this.database = FirebaseFirestore.getInstance();
        this.inviterToken = inviterToken;
    }

    public void loadChat(String selectedCategory, ICompleteCallback callback) {
        database.collection(Constants.KEY_COLLECTION_CATEGORIES)
                .document(selectedCategory)
                .collection(Constants.KEY_COLLECTION_CATEGORIES_CHATS)
                .whereEqualTo(Constants.KEY_CHAT_UID_SECOND_TOKEN, "")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // Подключаем второго юзера
                    Chat chat = null;
                    String documentId = null;
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Log.e("doc", document.getData().toString());
                        chat = document.toObject(Chat.class);
                        documentId = document.getId();
                        break;
                    }
                    if (chat != null && documentId != null) {
                        updateChat(selectedCategory, documentId);
                        callback.onComplete(new User(chat.getUid2Token()));
                    }
                })
                .addOnFailureListener(exception -> {
                    // Создаем новый чат
                    addChat(selectedCategory);
                });

    }

    private void addChat(String selectedCategory) {
        Chat chat = new Chat(Constants.KEY_CHAT_UID_FIRST_TOKEN, "");
        DocumentReference newChat = database.collection(Constants.KEY_COLLECTION_CATEGORIES)
                .document(selectedCategory)
                .collection(Constants.KEY_COLLECTION_CATEGORIES_CHATS)
                .document();

        newChat.set(chat)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Нет свободных комнат.... Ожидайте", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(error -> {
                    Log.e("AddChat", error.getMessage());
                });
        newChat.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "listen:error", error);
                return;
            }
            if (value != null && value.exists()) {
                Log.d(TAG, "Current data: " + value.getData());
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    public void updateChat(String selectedCategory, String documentId) {
        database.collection(Constants.KEY_COLLECTION_CATEGORIES)
                .document(selectedCategory)
                .collection(Constants.KEY_COLLECTION_CATEGORIES_CHATS)
                .document(documentId)
                .update(Constants.KEY_CHAT_UID_SECOND_TOKEN, inviterToken)
                .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Собеседник готов присоединиться", Toast.LENGTH_SHORT).show();
                            initiateMeeting(inviterToken);
                        }
                )
                .addOnFailureListener(error ->
                        Toast.makeText(context, "Неправильное подключение: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    public void initiateMeeting(String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);
            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken);

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
                            if (type.equals(Constants.REMOTE_MSG_INVITATION))
                                Toast.makeText(context, "Invitation sent successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

    }
}
