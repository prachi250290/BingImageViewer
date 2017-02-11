package com.testproject.bingimageviewer.webservice;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prachi on 11/02/17.
 */
public class ApiClient {

    public static final String BASE_URL = "https://api.cognitive.microsoft.com";

    public static Retrofit getClient(final HashMap<String, String> headers) {

        /// Validate Before Using
        if (headers != null && headers != null && headers.keySet().size() > 0) {
            headers.putAll(headers);
        }

        // Interceptor to pass Header Values
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request.Builder builder = chain.request().newBuilder();

                // Add Headers
                for (String key : headers.keySet()) {

                    String headerParam = headers.get(key);
                    builder.addHeader(key, headerParam);

                }

                Request request = builder.build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

}
