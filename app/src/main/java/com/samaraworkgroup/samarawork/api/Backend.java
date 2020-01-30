package com.samaraworkgroup.samarawork.api;

import com.samaraworkgroup.samarawork.model.Admin;
import com.samaraworkgroup.samarawork.model.AllChat;
import com.samaraworkgroup.samarawork.model.AllMessage;
import com.samaraworkgroup.samarawork.model.BonusesList;
import com.samaraworkgroup.samarawork.model.ChatId;
import com.samaraworkgroup.samarawork.model.Login;
import com.samaraworkgroup.samarawork.model.MyBonuses;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface Backend {

    @Multipart
    @POST("registration")
    Single<String> registration(@Header("Authorization") String key,
                                @Part("username") String username,
                                @Part("password") String password,
                                @Part("city") String city,
                                @Part("phone") String phone,
                                @Part("team") Integer team,
                                @Part("business_trip") Boolean trip,
                                @Part("specialist") String specialist,
                                @Part MultipartBody.Part photo,
                                @Part("device_token") String token);

    @Multipart
    @POST("app/login")
    Single<Login> login(@Part("phone") String email, @Part("password") String password, @Part("device_token") String token);

    @GET("api/bonuses/list")
    Single<BonusesList> getBonusesList(@Header("X-AUTH-TOKEN") String token);

    @Multipart
    @POST("api/bonuses/new")
    Single<String> newInvite(@Header("X-AUTH-TOKEN") String token,
                             @Part("amount") Integer amount,
                             @Part("type") String type,
                             @Part("phone") String phone,
                             @Part("name") String name,
                             @Part("speciality") String speciality);

    @Multipart
    @POST("api/bonuses/new")
    Single<String> newWork(@Header("X-AUTH-TOKEN") String token,
                           @Part("amount") Integer amount,
                           @Part("type") String type,
                           @Part("phone") String phone,
                           @Part("objectCity") String objectCity,
                           @Part("objectName") String objectName,
                           @Part("summ") String summ,
                           @Part MultipartBody.Part photo);


    @POST("api/bonuses/amount")
    Single<MyBonuses> getMyBonuses(@Header("X-AUTH-TOKEN") String key);

    @Multipart
    @POST("api/bonuses/bonus-request/add")
    Single<String> useMyBonuses(@Header("X-AUTH-TOKEN") String key,
                                @Part("amount") String amount,
                                @Part("category") String category,
                                @Part MultipartBody.Part photo);

    @POST("api/chat/get/all")
    Single<AllChat> getAllChat(@Header("X-AUTH-TOKEN") String key);

    @POST("api/chat/get/messages")
    Single<AllMessage> getAllMessage(@Header("X-AUTH-TOKEN") String key, @Body ChatId chatId);

    @Multipart
    @POST("api/chat/create/message")
    Single<String> sendMessage(@Header("X-AUTH-TOKEN") String key,
                               @Part("text") String text,
                               @Part("chat") Integer id,
                               @Part MultipartBody.Part photo);


    @POST("api/question/get-all")
    Single<Admin> getAllQuestion(@Header("X-AUTH-TOKEN") String key);

    @Multipart
    @POST("api/question/create")
    Single<String> sendQuestion(@Header("X-AUTH-TOKEN") String key,
                                @Part("question") String question,
                                @Part("object_address") String address,
                                @Part MultipartBody.Part photo);
}
