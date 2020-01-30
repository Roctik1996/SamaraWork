package com.samaraworkgroup.samarawork.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.MyBonuses;
import com.samaraworkgroup.samarawork.presenter.BonusesListPresenter;
import com.samaraworkgroup.samarawork.presenter.MyBonusesPresenter;
import com.samaraworkgroup.samarawork.view.BonusesListView;
import com.samaraworkgroup.samarawork.view.MyBonusesView;

import java.util.Objects;

import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class MyBonusActivity extends MvpAppCompatActivity implements OnLocaleChangedListener, MyBonusesView {

    private ProgressBar progressBar;
    private FrameLayout frame;
    private TextView money;
    private String myMoney;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @InjectPresenter
    MyBonusesPresenter myBonusesPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bonus);
        Button status1 = findViewById(R.id.btn_status);
        Button use1 = findViewById(R.id.btn_use);
        progressBar = findViewById(R.id.progress);
        money = findViewById(R.id.txt_money);
        frame = findViewById(R.id.frame);
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_TOKEN, "")).equals("")) {
                myBonusesPresenter.getMyBonuses(mSettings.getString(APP_PREFERENCES_TOKEN, ""));
            }
        }

        use1.setOnClickListener(view -> {
            Intent use = new Intent(this, UseBonusActivity.class);
            use.putExtra("money",myMoney);
            startActivity(use);
        });

        status1.setOnClickListener(view -> {
            Intent status = new Intent(this, StatusActivity.class);
            startActivity(status);
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getBonuses(MyBonuses myBonuses) {
        if (myBonuses!=null) {
            if (myBonuses.getAmountBonuses() == null) {
                money.setText("0 ГРН");
                myMoney = "0";
            } else {
                money.setText(myBonuses.getAmountBonuses() + " ГРН");
                myMoney = myBonuses.getAmountBonuses();
            }
        }
    }

    @Override
    public void showProgress(boolean isProgress) {
        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        frame.setVisibility(isProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
    }
}
