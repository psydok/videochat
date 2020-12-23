package ru.csu.videochat.network;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static HashMap<String, Retrofit> servers;

    public static Retrofit getClient(String url) {
        if (servers == null) {// ленивое объявление
            servers = new HashMap<String, Retrofit>();
        }

        if (!servers.containsKey(url)) {
            servers.put(url, new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build());
        }
        return servers.get(url);
    }
}
