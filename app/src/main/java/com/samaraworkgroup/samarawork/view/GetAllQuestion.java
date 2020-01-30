package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;
import com.samaraworkgroup.samarawork.model.Admin;

public interface GetAllQuestion extends MvpView {
    void getAll(Admin admin);
    void showProgress(boolean isProgress);
    void showError(String error);
}
