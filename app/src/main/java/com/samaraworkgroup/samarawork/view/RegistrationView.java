package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;

public interface RegistrationView extends MvpView {
    void registration(String token);
    void showProgress(boolean isProgress);
    void showError(String error);
}
