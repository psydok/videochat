package ru.csu.videochat.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Определяем api для отправки удаленных сообщений
 */
public interface IApiService {
    @POST("send")
    Call<String> sendRemoteMessage(
            @HeaderMap HashMap<String, String> headers,
            @Body String remoteBody
    );

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/themes/")
    Call<String> getThemes();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/charts/")
    Call<String> getCharts();

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json;charset=UTF-8"})
    @POST("auth/")
    Call<String> getAuthorization(String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json;charset=UTF-8"})
    @POST("register/")
    Call<String> getRegistration(String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("isinchat/")
    Call<String> isInChat(@Header("Authorization") String auth);




}
