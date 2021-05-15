package com.bsuir.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChartView;
import com.bsuir.quiz.R;
import com.bsuir.quiz.adapter.QuestionFragment;
import com.bsuir.quiz.model.Score;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TopActivity extends AppCompatActivity {

    private TextView firstUser;
    private TextView secondUser;
    private TextView thirdUser;
    private DatabaseReference ref;
    private FirebaseUser user;
    private List<Score> topUsers = TopicActivity.getTopUsers();
    private List<Score> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        firstUser = findViewById(R.id.top1);
        secondUser = findViewById(R.id.top2);
        thirdUser = findViewById(R.id.top3);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        displayTopUsers();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private List<Score> sortList() {
        scoreList = topUsers.stream().sorted(Comparator.comparing(Score::getValue).reversed()).collect(Collectors.toList());
        ref.child("Score").child("user").setValue(scoreList);
        for (int i = 0; i < scoreList.size(); i++) {
            int minutes = Integer.parseInt(scoreList.get(i).getTime()) / 1000 / 60;
            int seconds = (Integer.parseInt(scoreList.get(i).getTime()) / 1000) % 60;
            String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            scoreList.get(i).setTime(timeFormatted);
        }
        return scoreList;
    }

    private void displayTopUsers() {
        sortList();
        if (topUsers.size() == 1) {
            firstUser.setText("1) " + scoreList.get(0).toString());
        }
        if (topUsers.size() == 2) {
            firstUser.setText("1) " + scoreList.get(0).toString());
            secondUser.setText("2) " + scoreList.get(1).toString());
        }
        if(topUsers.size() ==3){
            firstUser.setText("1) " + scoreList.get(0).toString());
            secondUser.setText("2) " + scoreList.get(1).toString());
            thirdUser.setText("3) " + scoreList.get(2).toString());
        }
    }
}