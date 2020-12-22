package ru.csu.videochat.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.csu.videochat.interfaces.ICategoryListener;
import ru.csu.videochat.model.utilities.CategoryAdapter;
import ru.csu.videochat.model.utilities.Constants;
import ru.csu.videochat.network.ApiClient;
import ru.csu.videochat.network.CommunicationWithServer;
import ru.csu.videochat.network.IApiService;

public class CategoryModel {

    private Context context;

    public interface IComplete {
        void onComplete(boolean check);
    }

    public CategoryModel(Context context) {
        this.context = context;
    }

    public static CategoryModel getInstance(Context context) {
        return new CategoryModel(context);
    }

    public static void getCountChats(String category, CategoryAdapter.ICompleteCallback callback) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_CHATS)
                .whereEqualTo(Constants.KEY_CATEGORY_ID, category)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                            List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                            callback.onComplete(documents.size());
                        }
                )
                .addOnFailureListener(e -> {
                    callback.onComplete(-1);
                });
    }

    public void getThemes(ICategoryListener listener) {
        isHostAvailable(CommunicationWithServer.getServer(), check -> {
            if (check) {
                CommunicationWithServer.sendMessageThemes(listener);
            } else {
                listener.showThemes(new String[]{
                        "Знакомства",
                        "Фильмы",
                        "Игры",
                        "Общение",
                        "Книги",
                        "Помощь"
                });
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    )
                        return true;
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    /**
     * Check if host is reachable.
     *
     * @param server The host to check for availability. Can either be a machine name, such as "google.com",
     *               or a textual representation of its IP address, such as "8.8.8.8".
     */
    public static void isHostAvailable(final String server, IComplete listener) {
        try {
            ApiClient.getClient(server).create(IApiService.class)
                    .check().enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.e("@@@+", response.body());
                    } else {
                        Log.e("SendRemote-", response.message());
                    }
                    listener.onComplete(true);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("@@@-", t.getMessage());
                    listener.onComplete(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onComplete(false);
        }
    }
}
