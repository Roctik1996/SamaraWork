package com.samaraworkgroup.samarawork.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.samaraworkgroup.samarawork.R;

public class ContentActivity extends LocalizationActivity {

    Button getMoney, getBonus, myBonus,chat,admin;
    TextView phoneLeft, phoneRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        phoneLeft = findViewById(R.id.phone_left);
        phoneRight = findViewById(R.id.phone_right);
        getMoney = findViewById(R.id.get_money);
        getBonus = findViewById(R.id.get_bonus);
        myBonus = findViewById(R.id.my_bonus);
        chat = findViewById(R.id.chat);
        admin = findViewById(R.id.admin);

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

        getMoney.setOnClickListener(view -> {
            Intent getMoney = new Intent(this, GetMoneyActivity.class);
            startActivity(getMoney);
        });

        getBonus.setOnClickListener(view -> {
            Intent getBonus = new Intent(this, GetBonusActivity.class);
            startActivity(getBonus);
        });

        myBonus.setOnClickListener(view -> {
            Intent myBonus = new Intent(this, MyBonusActivity.class);
            startActivity(myBonus);
        });

        chat.setOnClickListener(view -> {
            Intent chat = new Intent(this, ChatListActivity.class);
            startActivity(chat);
        });

        admin.setOnClickListener(view -> {
            Intent admin = new Intent(this, AdminActivity.class);
            startActivity(admin);
        });


    }
}
