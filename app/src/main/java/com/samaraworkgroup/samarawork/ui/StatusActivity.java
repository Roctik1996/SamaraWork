package com.samaraworkgroup.samarawork.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.gson.Gson;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.BonusesData;
import com.samaraworkgroup.samarawork.model.BonusesList;
import com.samaraworkgroup.samarawork.model.Data;
import com.samaraworkgroup.samarawork.other.Const;
import com.samaraworkgroup.samarawork.presenter.BonusesListPresenter;
import com.samaraworkgroup.samarawork.ui.adapter.BonusAdapter;
import com.samaraworkgroup.samarawork.view.BonusesListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class StatusActivity extends MvpAppCompatActivity implements BonusesListView, OnLocaleChangedListener {

    private RecyclerView recyclerView;
    private ArrayList<Data> data;
    TextView ru, uk;
    private ProgressBar progressBar;
    private FrameLayout frame;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @InjectPresenter
    BonusesListPresenter bonusesListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);
        ru = findViewById(R.id.txt_ru);
        uk = findViewById(R.id.txt_uk);

        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_TOKEN, "")).equals("")) {
                bonusesListPresenter.getBonusesList(mSettings.getString(APP_PREFERENCES_TOKEN, ""));
            }
        }

        data = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (Const.lng.equals("uk")) {
            uk.setTextColor(getResources().getColor(R.color.defText));
            ru.setTextColor(getResources().getColor(R.color.textColor));
        } else if (Const.lng.equals("ru")) {
            ru.setTextColor(getResources().getColor(R.color.defText));
            uk.setTextColor(getResources().getColor(R.color.textColor));
        }
        uk.setOnClickListener((v) -> {
            uk.setTextColor(getResources().getColor(R.color.textColor));
            ru.setTextColor(getResources().getColor(R.color.defText));
            setLanguage("uk");
            Const.lng = "uk";
        });

        ru.setOnClickListener((v) -> {
            ru.setTextColor(getResources().getColor(R.color.textColor));
            uk.setTextColor(getResources().getColor(R.color.defText));
            setLanguage("ru");
            Const.lng = "ru";
        });
    }

    @Override
    public void getBonusesList(BonusesList bonusesList) {
        if (bonusesList != null) {
            if (bonusesList.getDataMap() != null) {
                ArrayList<String> key = new ArrayList<>(bonusesList.getDataMap().keySet());
                for (int i = 0; i < key.size(); i++) {
                    data.add(new Data(stringToArray(bonusesList.getDataMap().values().toString(), BonusesData[].class).get(i).getPhone(),
                            String.valueOf(stringToArray(bonusesList.getDataMap().values().toString(), BonusesData[].class).get(i).getAmount()),
                            stringToArray(bonusesList.getDataMap().values().toString(), BonusesData[].class).get(i).getStatus()));
                }
                BonusAdapter adapter = new BonusAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    @Override
    public void showProgress(boolean isProgress) {
        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        frame.setVisibility(isProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, getString(R.string.error_money), Toast.LENGTH_LONG).show();
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

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }
}
