package ru.csu.videochat.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Определяем api для отправки удаленных сообщений
 */
public interface IApiService {
    // POST запросы
    @POST("send")
    Call<String> sendRemoteMessage(
            @HeaderMap HashMap<String, String> headers,
            @Body String remoteBody
    );

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("joinChat")
    Call<String> joinChat(String body);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json;charset=UTF-8"})
    @POST("register")
    Call<String> getRegistration(String body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("addLinks")
    Call<String> addLinks(String body);

    // GET запросы
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/")
    Call<String> check();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/themes")
    Call<String> getThemes();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/charts")
    Call<String> getCharts(String body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("leaveChat")
    Call<String> leaveChat(String body);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json;charset=UTF-8"})
    @GET("auth")
    Call<String> getAuthorization(String body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("isInChat")
    Call<String> isInChat(@Header("Authorization") String body);

}
