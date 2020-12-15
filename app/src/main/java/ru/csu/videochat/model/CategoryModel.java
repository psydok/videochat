package ru.csu.videochat.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import ru.csu.videochat.interfaces.ICategoryListener;
import ru.csu.videochat.model.utilities.CategoryAdapter;
import ru.csu.videochat.model.utilities.Constants;
import ru.csu.videochat.network.CommunicationWithServer;

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

    public void getThemes(ICategoryListener listener) {
        if (isHostAvailable(CommunicationWithServer.getHost(), 80, 2000))
            CommunicationWithServer.sendMessageThemes(listener);
        else listener.showThemes(new String[]{
                "Знакомства",
                "Фильмы",
                "Игры",
                "Общение",
                "Книги",
                "Помощь"
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
     * @param host    The host to check for availability. Can either be a machine name, such as "google.com",
     *                or a textual representation of its IP address, such as "8.8.8.8".
     * @param port    The port number.
     * @param timeout The timeout in milliseconds.
     * @return True if the host is reachable. False otherwise.
     */
    public static boolean isHostAvailable(final String host, final int port, final int timeout) {
        try (final Socket socket = new Socket()) {
            final InetAddress inetAddress = InetAddress.getByName(host);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);

            socket.connect(inetSocketAddress, timeout);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
