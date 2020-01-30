package com.samaraworkgroup.samarawork.network;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.samaraworkgroup.samarawork.BuildConfig;
import com.samaraworkgroup.samarawork.api.Backend;
import com.samaraworkgroup.samarawork.model.BonusesList;
import com.samaraworkgroup.samarawork.model.ResponseDeserializer;
import com.samaraworkgroup.samarawork.other.Const;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class NetworkModule {
    private static volatile Retrofit sRetrofit;

    private static OkHttpClient generateOkHttpClient() {
        HttpLoggingInterceptorHeavyFilesSafe httpLoggingInterceptor = new HttpLoggingInterceptorHeavyFilesSafe();
        HttpLoggingInterceptorHeavyFilesSafe.Level logLevel = BuildConfig.DEBUG ?
                HttpLoggingInterceptorHeavyFilesSafe.Level.BODY : HttpLoggingInterceptorHeavyFilesSafe.Level.NONE;
        httpLoggingInterceptor.setLevel(logLevel);
        return new OkHttpClient.Builder()

                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(new TypeToken<BonusesList>() {
                }.getType(), new ResponseDeserializer())
                .create();
        return new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(generateOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Backend getBackEndService() {
        return getRetrofit().create(Backend.class);
    }

}
