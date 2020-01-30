package com.samaraworkgroup.samarawork.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.Admin;
import com.samaraworkgroup.samarawork.presenter.GetQuestionPresenter;
import com.samaraworkgroup.samarawork.ui.adapter.QuestionListAdapter;
import com.samaraworkgroup.samarawork.view.GetAllQuestion;

import java.util.Objects;

import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class AdminActivity extends MvpAppCompatActivity implements GetAllQuestion, OnLocaleChangedListener {

    private ProgressBar progressBar;
    private FrameLayout frame;
    private RecyclerView recyclerView;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @InjectPresenter
    GetQuestionPresenter getQuestionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_TOKEN, "")).equals("")) {
                getQuestionPresenter.getQuestion(mSettings.getString(APP_PREFERENCES_TOKEN, ""));
            }
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent question = new Intent(this, SendQuestionActivity.class);
            startActivity(question);
        });
    }

    @Override
    public void getAll(Admin admin) {
        if (admin!=null) {
            QuestionListAdapter adapter = new QuestionListAdapter(this, admin.getQuestions());
            recyclerView.setAdapter(adapter);
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

    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
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
}
