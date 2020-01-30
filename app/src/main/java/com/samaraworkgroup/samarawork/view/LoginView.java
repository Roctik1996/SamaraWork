package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;
import com.samaraworkgroup.samarawork.model.Login;

public interface LoginView extends MvpView {
    void getLogin(Login login);
    void showProgress(boolean isProgress);
    void showError(String error);
}
