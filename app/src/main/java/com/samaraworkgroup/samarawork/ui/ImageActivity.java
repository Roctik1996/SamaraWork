package com.samaraworkgroup.samarawork.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samaraworkgroup.samarawork.R;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView = findViewById(R.id.image);

        Glide.with(this)
                .load(getIntent().getStringExtra("photo"))
                .apply(new RequestOptions().fitCenter())
                .into(imageView);
    }
}
