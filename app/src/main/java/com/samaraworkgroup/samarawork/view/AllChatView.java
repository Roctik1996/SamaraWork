package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;
import com.samaraworkgroup.samarawork.model.AllChat;

public interface AllChatView extends MvpView {
    void getAll(AllChat allChat);
    void showProgress(boolean isProgress);
    void showError(String error);
}
