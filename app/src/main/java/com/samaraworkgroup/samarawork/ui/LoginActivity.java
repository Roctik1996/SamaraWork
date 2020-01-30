package com.samaraworkgroup.samarawork.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.Login;
import com.samaraworkgroup.samarawork.presenter.LoginPresenter;
import com.samaraworkgroup.samarawork.view.LoginView;
import com.santalu.maskedittext.MaskEditText;

import java.util.Objects;


import static android.text.TextUtils.isEmpty;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_ID;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_PHONE;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_PASS;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class LoginActivity extends MvpAppCompatActivity implements LoginView, OnLocaleChangedListener {

    private AppCompatEditText pass;
    private MaskEditText phone;
    private SharedPreferences mSettings;
    private ProgressBar progressBar;
    private FrameLayout frame;

    @InjectPresenter
    LoginPresenter loginPresenter;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.btn_login);
        phone = findViewById(R.id.edt_phone);
        pass = findViewById(R.id.edt_pass);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        if (mSettings.contains(APP_PREFERENCES_PHONE) && mSettings.contains(APP_PREFERENCES_PASS)) {
            if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_PHONE, "")).equals("") && !Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_PASS, "")).equals("")) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                    loginPresenter.login(mSettings.getString(APP_PREFERENCES_PHONE, ""), mSettings.getString(APP_PREFERENCES_PASS, ""),instanceIdResult.getToken());
                });

            }
        }

        login.setOnClickListener(view -> {
            if (validate(Objects.requireNonNull(phone.getText()).toString(), Objects.requireNonNull(pass.getText()).toString())) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                    loginPresenter.login("+38"+phone.getRawText(), pass.getText().toString(),instanceIdResult.getToken());
                });
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_PHONE, "+38"+phone.getRawText());
                editor.putString(APP_PREFERENCES_PASS, pass.getText().toString());
                editor.apply();
            }
        });
    }

    @Override
    public void getLogin(Login login) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_TOKEN, login.getToken());
        editor.putString(APP_PREFERENCES_ID, String.valueOf(login.getUserId()));
        editor.apply();
        Intent content = new Intent(this, ContentActivity.class);
        startActivity(content);
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

    private boolean validate(String email, String password) {
        if (isEmpty(email)) {
            Toast.makeText(this, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(password)) {
            Toast.makeText(this, getString(R.string.error_pass), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
