package com.mindwarriorhack.app.services;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private Retrofit retrofit = null;

    public Api getApi(){

        /*String BASE_URL="https://mindwarriorhacks.app/test/api/Api/";*/
        String BASE_URL="https://mindwarriorhacks.app/uat/api/Api/";
        //String BASE_URL = "https://mindwarriorhacks.app/admin/api/Api/";

        if (retrofit == null){
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .client(okClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(Api.class);
    }

    private static OkHttpClient okClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                String url = original.url().toString();
                url = url.replace("%3F", "?");
                url = url.replace("%3D", "=");

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()

                        .url(url);
                        //.header("Authorization", "auth-value"); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return httpClient.build();
    }
}
