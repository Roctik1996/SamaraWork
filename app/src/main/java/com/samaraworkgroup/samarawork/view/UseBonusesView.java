package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;
import com.samaraworkgroup.samarawork.model.MyBonuses;

public interface UseBonusesView extends MvpView {
    void useBonuses(String useBonuses);
    void showProgress(boolean isProgress);
    void showError(String error);
}
