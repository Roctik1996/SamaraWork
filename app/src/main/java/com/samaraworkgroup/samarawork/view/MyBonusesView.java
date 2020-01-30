package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;
import com.samaraworkgroup.samarawork.model.BonusesList;
import com.samaraworkgroup.samarawork.model.MyBonuses;

public interface MyBonusesView extends MvpView {
    void getBonuses(MyBonuses myBonuses);
    void showProgress(boolean isProgress);
    void showError(String error);
}
