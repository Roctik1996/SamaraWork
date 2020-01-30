package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;
import com.samaraworkgroup.samarawork.model.AllMessage;

public interface AllMessageView extends MvpView {
    void getAll(AllMessage allMessage);
    void showProgress(boolean isProgress);
    void showError(String error);
}
