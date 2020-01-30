package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;

public interface SendQuestionView extends MvpView {
    void send(String success);
    void showError(String error);
}
