package ru.csu.videochat.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.csu.videochat.interfaces.ICategoryListener;
import ru.csu.videochat.model.entries.Category;
import ru.csu.videochat.model.entries.MessageCategories;
import ru.csu.videochat.model.utilities.Constants;

public class CommunicationWithServer {
    private final static String server = "http://88.206.94.139";
    private final static String host = "88.206.94.139";

    public static String getHost() {
        return host;
    }

    public static String getServer() {
        return server;
    }

    public static void sendMessageRegister(String email, String password) {
        try {
            JSONObject body = getBody(email, password);
            ApiClient.getClient(server).create(IApiService.class)
                    .getRegistration(body.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Log.e("@@@+",  response.body());
                            } else {
                                Log.e("SendRemote-", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("@@@", call.toString());
                            Log.e("@@@", t.getMessage());
                        }
                    });
        } catch (Exception exception) {
        }
    }

    public static void sendMessageAuth(String email, String password) {
        try {
            JSONObject body = getBody(email, password);
            ApiClient.getClient(server).create(IApiService.class)
                    .getAuthorization(body.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Log.e("@@@+",  response.body());
                            } else {
                                Log.e("SendRemote-", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("@@@", call.toString());
                            Log.e("@@@", t.getMessage());
                        }
                    });
        } catch (Exception exception) {
        }
    }

    public static void sendMessageThemes(ICategoryListener listener) {
        try {
            sendMessageThemeServer(listener);
        } catch (Exception exception) {
        }
    }

    private static JSONObject getBody(String email, String password) {
        try {
            JSONObject body = new JSONObject();
            body.put(Constants.KEY_LOGIN, email);
            body.put(Constants.KEY_PASSWORD, password);
            return body;
        } catch (Exception exception) {
            Log.e("getBody", exception.getMessage());
            return null;
        }
    }

    private static void sendMessageThemeServer(ICategoryListener listener) {
        ApiClient.getClient(server).create(IApiService.class)
                .getThemes()
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String themes = response.body();
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            MessageCategories messageCategories = gson.fromJson(themes, MessageCategories.class);
                            Category[] categories = messageCategories.getCategories();
                            String[] names = new String[categories.length];
                            for (int i = 0; i < categories.length; i++) {
                                names[i] = categories[i].getName();
                            }
                            listener.showThemes(names);
                            Log.e("@@@", names.toString());
                        } else {
                            Log.e("SendServer", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("@@@1", call.toString());
                        Log.e("@@@2", t.getMessage());
                    }
                });
    }
}