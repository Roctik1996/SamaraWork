package com.samaraworkgroup.samarawork.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.presenter.InvitePresenter;
import com.samaraworkgroup.samarawork.view.InviteView;
import com.santalu.maskedittext.MaskEditText;

import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.text.TextUtils.isEmpty;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES;
import static com.samaraworkgroup.samarawork.other.Const.APP_PREFERENCES_TOKEN;

public class GetMoneyActivity extends MvpAppCompatActivity implements OnLocaleChangedListener, InviteView {

    Button send;
    private TextView name;
    private MaskEditText phone;
    private MaterialSpinner spec;
    private ProgressBar progressBar;
    private FrameLayout frame;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @InjectPresenter
    InvitePresenter invitePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money);
        send = findViewById(R.id.btn_send);
        name = findViewById(R.id.edt_name);
        phone = findViewById(R.id.edt_phone);
        spec = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);

        send.setOnClickListener(view -> {
            if (validate(name.getText().toString(),phone.getRawText(),spec.getSelectedItemPosition())){
                SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                if (mSettings.contains(APP_PREFERENCES_TOKEN)) {
                    if (!Objects.requireNonNull(mSettings.getString(APP_PREFERENCES_TOKEN, "")).equals("")) {
                        invitePresenter.newInvite(mSettings.getString(APP_PREFERENCES_TOKEN, ""),
                                1000,
                                "invite",
                                "+38"+phone.getRawText(),
                                name.getText().toString(),
                                spec.getSelectedItem().toString());
                    }
                }
            }
        });
    }

    private boolean validate(String name, String phone, int spec) {
        if (isEmpty(name)) {
            Toast.makeText(this, getString(R.string.error_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spec == 0) {
            Toast.makeText(this, getString(R.string.error_spec), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.samaraworkgroup.samarawork";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getString(R.string.bonus_invite))
                .setContentText(getString(R.string.success))
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
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
    public void invite(String success) {
        sendNotification();
        Intent back = new Intent(this, ContentActivity.class);
        startActivity(back);
        finish();
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
