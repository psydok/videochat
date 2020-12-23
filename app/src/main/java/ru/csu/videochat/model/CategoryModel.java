package ru.csu.videochat.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
        isServerAvailable(CommunicationWithServer.getServer(), check -> {
            if (check) {
                CommunicationWithServer.sendMessageThemes(listener);
            } else {
                List<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
                pairs.add(new Pair<>("Спорт", "https://gtrk-saratov.ru/wp-content/uploads/2019/12/5f2f983b1d838ee26be6b93a38fa378c.jpg"));
                pairs.add(new Pair<>("Кино", "https://cdn22.img.ria.ru/images/91548/33/915483359_0:203:3888:2390_600x0_80_0_0_d82704e34388d68bdcf57766e7b0b361.jpg"));
                pairs.add(new Pair<>("Игры", "https://www.goodnewsfinland.com/wp-content/uploads/2019/08/Games-765x430.png"));
                pairs.add(new Pair<>("Кулинария", "https://recipesbook.ru/uploads/posts/2012-08/1345130069_finskaya-kuhnya.jpeg"));
                pairs.add(new Pair<>("Литература", "https://storage.theoryandpractice.ru/tnp/uploads/image_unit/000/132/309/image/base_93959877b0.jpg"));
                pairs.add(new Pair<>("Музыка", "https://m.iguides.ru/upload/iblock/3a0/3a0569a7237bbfb5406580deed8a9958.jpg"));
                listener.showThemes(pairs);
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
    public static void isServerAvailable(final String server, IComplete listener) {
        try {
            ApiClient.getClient(server).create(IApiService.class)
                    .check().enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("isAvailable", response.message() + " code: " + response.code());
                    listener.onComplete(true);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("isAvailable", "onFailure: " + t.getMessage());
                    listener.onComplete(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onComplete(false);
        }
    }
}
