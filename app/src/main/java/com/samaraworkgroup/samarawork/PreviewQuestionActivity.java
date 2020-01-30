package com.samaraworkgroup.samarawork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samaraworkgroup.samarawork.other.Const;
import com.samaraworkgroup.samarawork.ui.ImageActivity;

public class PreviewQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_question);
        TextView answer = findViewById(R.id.txt_answer_body);
        TextView question = findViewById(R.id.txt_question_body);
        TextView city = findViewById(R.id.txt_city_body);
        ImageView imageAnswer = findViewById(R.id.image_answer);
        ImageView imageQuestion = findViewById(R.id.image_question);

        question.setText(getIntent().getStringExtra("question"));
        city.setText(getIntent().getStringExtra("address"));

        if (getIntent().getStringExtra("answer").equals(""))
            answer.setText(R.string.no_answer);
        else
            answer.setText(getIntent().getStringExtra("answer"));

        if (!getIntent().getStringExtra("image_answer").equals("")) {
            imageAnswer.setVisibility(View.VISIBLE);
            Glide.with(this).load("https://"+getIntent().getStringExtra("image_answer"))
                    .apply(new RequestOptions().centerCrop())
                    .into(imageAnswer);
        }
        imageAnswer.setOnClickListener(v ->{
            Intent photo = new Intent(this, ImageActivity.class);
            photo.putExtra("photo", "https://"+getIntent().getStringExtra("image_answer"));
            startActivity(photo);
        });

        if (!getIntent().getStringExtra("image_question").equals("")) {
            imageQuestion.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load("https://"+getIntent().getStringExtra("image_question"))
                    .apply(new RequestOptions().centerCrop())
                    .into(imageQuestion);
        }

        imageQuestion.setOnClickListener(v ->{
            Intent photo = new Intent(this, ImageActivity.class);
            photo.putExtra("photo", "https://"+getIntent().getStringExtra("image_question"));
            startActivity(photo);
        });

    }
}
