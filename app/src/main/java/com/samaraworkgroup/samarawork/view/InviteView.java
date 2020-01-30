package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;

public interface InviteView extends MvpView {
    void invite(String success);
    void showProgress(boolean isProgress);
    void showError(String error);
}
