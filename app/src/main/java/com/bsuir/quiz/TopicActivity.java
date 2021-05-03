package com.bsuir.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import static java.security.AccessController.getContext;

public class TopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        TextView text = findViewById(R.id.text);
        Animation textUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_anim);
        text.startAnimation(textUp);
        text.setVisibility(View.VISIBLE);
    }
}