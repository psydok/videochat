package ru.csu.videochat.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ru.csu.videochat.model.utilities.CategoryAdapter;
import ru.csu.videochat.model.utilities.Constants;

public class CategoryModel {
    private Context context;

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


}
