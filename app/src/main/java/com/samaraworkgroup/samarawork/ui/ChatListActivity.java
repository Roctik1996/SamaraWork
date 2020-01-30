package com.samaraworkgroup.samarawork.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.AllChat;
import com.samaraworkgroup.samarawork.model.ChatList;
import com.samaraworkgroup.samarawork.model.SortedChat;
import com.samaraworkgroup.samarawork.presenter.AllChatPresenter;
import com.samaraworkgroup.samarawork.ui.adapter.ChatListAdapter;
import com.samaraworkgroup.samarawork.view.AllChatView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class ChatListActivity extends MvpAppCompatActivity implements AllChatView, OnLocaleChangedListener {
    private RecyclerView recyclerView;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    private TextView txtNoData;
    private ProgressBar progressBar;
    private FrameLayout frame;

    @InjectPresenter
    AllChatPresenter allChatPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        txtNoData = findViewById(R.id.txt_no_data);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_TOKEN, "")).equals("")) {
                allChatPresenter.getChatList(mSettings.getString(APP_PREFERENCES_TOKEN, ""));
            }
        }

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

    @Override
    public void getAll(AllChat allChat) {
        if (allChat != null) {
            if (allChat.getChatList()!=null) {
                txtNoData.setVisibility(View.INVISIBLE);
                Collections.sort(allChat.getChatList(), new SortedChat());
                ChatListAdapter adapter = new ChatListAdapter(this, allChat.getChatList());
                recyclerView.setAdapter(adapter);
            }
            else txtNoData.setVisibility(View.VISIBLE);
        } else
            txtNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(boolean isProgress) {
        progressBar.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        frame.setVisibility(isProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {

    }
}
