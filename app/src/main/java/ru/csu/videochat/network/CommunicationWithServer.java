package ru.csu.videochat.network;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.csu.videochat.activities.auth.AuthActivity;
import ru.csu.videochat.interfaces.ICategoryListener;
import ru.csu.videochat.model.entries.Category;
import ru.csu.videochat.model.entries.MessageAuth;
import ru.csu.videochat.model.entries.MessageCategories;
import ru.csu.videochat.model.entries.StatusServer;
import ru.csu.videochat.model.utilities.CategoryAdapter;
import ru.csu.videochat.model.utilities.Constants;
import ru.csu.videochat.model.utilities.PreferenceManager;

public class CommunicationWithServer {
    private final static String server = "http://88.206.94.139:8080";
    private final static String host = "88.206.94.139";

    public static String getHost() {
        return host;
    }

    public static String getServer() {
        return server;
    }

    private static void saveToken(String responseBody) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        MessageAuth messageAuth = gson.fromJson(responseBody, MessageAuth.class);
        String token = messageAuth.getToken();
        PreferenceManager.putServerToken(AuthActivity.contextApp, token);
    }

    private static int getStatusMessage(String body) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        StatusServer messageAuth = gson.fromJson(body, StatusServer.class);
        int status = messageAuth.getStatus();
        return status;
    }

    public static void sendMessageRegister(String email, String password) {
        try {
            JSONObject body = getBody(email, password);
            ApiClient.getClient(server).create(IApiService.class)
                    .getRegistration(body.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String body = response.body();
                            if (getStatusMessage(body) == 1) {
                                saveToken(body);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("MessageRegister", "onFailure" + call.toString());
                            Log.e("MessageRegister", "onFailure" + t.getMessage());
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
                                String body = response.body();
                                if (getStatusMessage(body) == 1) {
                                    saveToken(body);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("MessageAuth", "onFailure: " + call.toString());
                            Log.e("MessageAuth", "onFailure: " + t.getMessage());
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

    private static MessageCategories getResponseMessageCategories(String body) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        MessageCategories messageCategories = gson.fromJson(body, MessageCategories.class);
        return messageCategories;
    }

    private static void sendMessageThemeServer(ICategoryListener listener) {
        ApiClient.getClient(server).create(IApiService.class)
                .getThemes()
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String body = response.body();
                            List<Pair<String, String>> themes = new ArrayList<>();
                            if (getStatusMessage(body) != 1) {
                                listener.showThemes(Constants.getCategory());
                                return;
                            }
                            MessageCategories messageCategories = getResponseMessageCategories(body);
                            Category[] categories = messageCategories.getThemes();
                            for (int i = 0; i < categories.length; i++) {
                                themes.add(new Pair<>(categories[i].getName(), server + "/"
                                        + categories[i].getImage().replace("\\", "")));
                            }
                            listener.showThemes(themes);
                        } else {
                            Log.e("ThemeServer", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("ThemeServer", "onFailure: " + call.toString());
                        Log.e("ThemeServer", "onFailure: " + t.getMessage());
                    }
                });
    }

    public static void sendMessageLink(String token, String link) {
        JSONObject body = new JSONObject();
        try {
            body.put(Constants.KEY_TOKEN, token);
            body.put(Constants.KEY_LINK, link);
        } catch (Exception exception) {
            Log.e("BodyLink", exception.getMessage());
        }

        ApiClient.getClient(server).create(IApiService.class)
                .updateUser(body.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String body = response.body();
                            if (getStatusMessage(body) == 1) {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("MessageLink", "onFailure: " + call.toString());
                        Log.e("MessageLink", "onFailure: " + t.getMessage());
                    }
                });
    }
}
