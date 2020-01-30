package com.samaraworkgroup.samarawork.view;

import com.arellomobile.mvp.MvpView;
import com.samaraworkgroup.samarawork.model.BonusesList;

public interface BonusesListView extends MvpView {
    void getBonusesList(BonusesList bonusesList);
    void showProgress(boolean isProgress);
    void showError(String error);
}
