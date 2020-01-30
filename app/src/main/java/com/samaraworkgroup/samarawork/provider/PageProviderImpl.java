package com.samaraworkgroup.samarawork.provider;

import com.samaraworkgroup.samarawork.api.Backend;
import com.samaraworkgroup.samarawork.model.Admin;
import com.samaraworkgroup.samarawork.model.AllChat;
import com.samaraworkgroup.samarawork.model.AllMessage;
import com.samaraworkgroup.samarawork.model.BonusesList;
import com.samaraworkgroup.samarawork.model.ChatId;
import com.samaraworkgroup.samarawork.model.Login;
import com.samaraworkgroup.samarawork.model.MyBonuses;
import com.samaraworkgroup.samarawork.network.NetworkModule;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public class PageProviderImpl {
    private Backend mBackendService;

    PageProviderImpl() {
        initNetworkModule();
    }

    private void initNetworkModule() {
        mBackendService = NetworkModule.getBackEndService();
    }

    public Single<Login> login(String email, String password, String token) {
        return mBackendService.login(email, password, token);
    }


    public Single<String> signup(String key,
                                 String username,
                                 String password,
                                 String city,
                                 String phone,
                                 Integer team,
                                 Boolean trip,
                                 String specialist,
                                 MultipartBody.Part photo,
                                 String token) {
        return mBackendService.registration(key, username, password, city, phone, team, trip, specialist, photo,token);
    }

    public Single<BonusesList> getBonusesList(String token) {
        return mBackendService.getBonusesList(token);
    }

    public Single<String> invite(String token,
                                 Integer amount,
                                 String type,
                                 String phone,
                                 String name,
                                 String speciality) {
        return mBackendService.newInvite(token, amount, type, phone, name, speciality);
    }

    public Single<String> work(String token,
                               Integer amount,
                               String type,
                               String phone,
                               String objectCity,
                               String objectName,
                               String summ,
                               MultipartBody.Part photo) {
        return mBackendService.newWork(token, amount, type,phone, objectCity, objectName, summ, photo);
    }

    public Single<MyBonuses> getMyBonuses(String token) {
        return mBackendService.getMyBonuses(token);
    }

    public Single<String> useMyBonuses(String token,String amount, String category,MultipartBody.Part photo) {
        return mBackendService.useMyBonuses(token, amount, category,photo);
    }

    public Single<AllChat> getAllChat(String token) {
        return mBackendService.getAllChat(token);
    }

    public Single<AllMessage> getAllMessage(String token, ChatId id) {
        return mBackendService.getAllMessage(token, id);
    }

    public Single<String> sendMessage(String token, String text, Integer id, MultipartBody.Part photo) {

        return mBackendService.sendMessage(token, text, id, photo);
    }


    public Single<Admin> getAllQuestion(String token) {
        return mBackendService.getAllQuestion(token);
    }

    public Single<String> sendQuestion(String token,
                                       String question,
                                       String address,
                                       MultipartBody.Part photo) {
        return mBackendService.sendQuestion(token, question, address, photo);
    }

}
