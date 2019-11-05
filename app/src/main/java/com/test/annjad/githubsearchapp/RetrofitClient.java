package com.test.annjad.githubsearchapp;

import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClient {

    public static Retrofit retrofit;
    public static Moshi moshi = new Moshi.Builder().build();
    private static final String BASE_URL = "https://api.github.com/search/";

    public static Retrofit getRetrofitClient() {

        /*
        The HttpLoggingInterceptor and the OkHttpClient are used for testing only, we can monitor the API requests and responses with this.
        Add the library logging-interceptor like this: implementation 'com.squareup.okhttp3:logging-interceptor:3.10.0' .
        Search in the Logcat for the tag OkHttp to get all API requests and responses, and search for okhttp: link to get the api request only
        */

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}
