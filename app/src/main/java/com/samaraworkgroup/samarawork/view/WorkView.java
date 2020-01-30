package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;

public interface WorkView extends MvpView {
    void work(String success);
    void showProgress(boolean isProgress);
    void showError(String error);
}
