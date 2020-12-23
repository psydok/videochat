package ru.csu.videochat.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    @POST("api/joinChat")
    Call<String> joinChat(@Body String body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("api/updateUser")
    Call<String> updateUser(@Body String body);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json;charset=UTF-8"})
    @POST("api/register")
    Call<String> getRegistration(@Body String body);

    // GET запросы
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/")
    Call<String> check();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/themes")
    Call<String> getThemes();

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/charts")
    Call<String> getCharts(@Body String body);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/leaveChat")
    Call<String> leaveChat(@Body String body);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json;charset=UTF-8"})
    @GET("api/auth")
    Call<String> getAuthorization(@Body String body);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json;charset=UTF-8"})
    @GET("resources/themes/{image}")
    Call<String> getImage(@Path("image") String image);
}
