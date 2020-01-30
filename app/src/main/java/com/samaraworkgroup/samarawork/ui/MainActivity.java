package com.samaraworkgroup.samarawork.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.ChatList;
import com.samaraworkgroup.samarawork.model.SortedChat;
import com.samaraworkgroup.samarawork.other.Const;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends LocalizationActivity {

    Button about, rules, signUp, login;
    TextView phoneLeft, phoneRight, ru, uk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        about = findViewById(R.id.btn_about_us);
        rules = findViewById(R.id.btn_rules);
        signUp = findViewById(R.id.btn_reg);
        login = findViewById(R.id.btn_login);
        phoneLeft = findViewById(R.id.phone_left);
        phoneRight = findViewById(R.id.phone_right);
        ru = findViewById(R.id.txt_ru);
        uk = findViewById(R.id.txt_uk);
        Const.lng = String.valueOf(getCurrentLanguage());

        if (Const.lng.equals("uk"))
        {
            uk.setTextColor(getResources().getColor(R.color.defText));
            ru.setTextColor(getResources().getColor(R.color.textColor));
        }else if (Const.lng.equals("ru")){
            ru.setTextColor(getResources().getColor(R.color.defText));
            uk.setTextColor(getResources().getColor(R.color.textColor));
        }
        uk.setOnClickListener((v)->{
            uk.setTextColor(getResources().getColor(R.color.textColor));
            ru.setTextColor(getResources().getColor(R.color.defText));
            setLanguage("uk");
        });

        ru.setOnClickListener((v)->{
            ru.setTextColor(getResources().getColor(R.color.textColor));
            uk.setTextColor(getResources().getColor(R.color.defText));
            setLanguage("ru");
        });

        about.setOnClickListener(view -> {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
        });

        rules.setOnClickListener(view -> {
            Intent rules = new Intent(this, RulesActivity.class);
            startActivity(rules);
        });

        signUp.setOnClickListener(view -> {
            Intent signUp = new Intent(this, RegistrationActivity.class);
            startActivity(signUp);
        });

        login.setOnClickListener(view -> {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        });

        phoneLeft.setOnClickListener((v) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0679300925"));
            startActivity(intent);
        });

        phoneRight.setOnClickListener((v) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0639484602"));
            startActivity(intent);
        });
    }
}
