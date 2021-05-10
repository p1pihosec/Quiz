package com.bsuir.quiz.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.bsuir.quiz.R;
import com.bsuir.quiz.adapter.QuestionFragment;
import com.bsuir.quiz.model.Score;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PieChartActivity extends AppCompatActivity {

    private AnyChartView anyChartView;
    private TextView textViewTime;
    private TextView firstUser;
    private TextView secondUser;
    private TextView thirdUser;
    private String[] answers = {"Correct", "Wrong", "Unanswered"};
    private static int[] amountOfAnswers;
    private DatabaseReference ref;
    private FirebaseUser user;
    private List<Score> topUsers = TopicActivity.getTopUsers();
    private Score score;
    private List<Score> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        anyChartView = findViewById(R.id.any_chart_view);
        textViewTime = findViewById(R.id.transit_time);
        firstUser = findViewById(R.id.first_user);
        secondUser = findViewById(R.id.second_user);
        thirdUser = findViewById(R.id.third_user);

        user = FirebaseAuth.getInstance().getCurrentUser();
        score = new Score(user.getEmail(), (long) amountOfAnswers[0],
                String.valueOf(QuestionFragment.getTransitTime()));
        ref = FirebaseDatabase.getInstance().getReference();
        long transitTime = QuestionFragment.getTransitTime();
        int minutes = (int) (transitTime / 1000) / 60;
        int seconds = (int) (transitTime / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewTime.setText("Transit time: " + timeFormatted);
        setupPieChart();
        displayTopUsers();
        QuestionFragment.setCorrectAnswer(amountOfAnswers[0]);
        QuestionFragment.setWrongAnswer(amountOfAnswers[1]);
        QuestionFragment.setTransitTime(transitTime);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private List<Score> sortList() {
        scoreList = topUsers.stream().sorted(Comparator.comparing(Score::getValue).reversed().thenComparing(Score::getTime)).collect(Collectors.toList());
        ref.child("Score").child("user").setValue(scoreList);
        for (int i = 0; i < scoreList.size(); i++) {
            int minutes = (int) (Integer.parseInt(scoreList.get(i).getTime()) / 1000) / 60;
            int seconds = (int) (Integer.parseInt(scoreList.get(i).getTime()) / 1000) % 60;
            String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            scoreList.get(i).setTime(timeFormatted);
        }
        return scoreList;
    }

    private void displayTopUsers() {
        int indexOfRemovedElement = -1;
        if (topUsers.size() < 3) {
            addUser();
        }
        else if (topUsers.size() == 3) {
            for (int i = topUsers.size() - 1; i >= 0; i--) {
                if ((Long.parseLong(topUsers.get(i).getTime()) <= QuestionFragment.getTransitTime() &&
                        topUsers.get(i).getName().equals(user.getEmail())) ||
                        (Integer.parseInt(String.valueOf(topUsers.get(i).getValue())) <= amountOfAnswers[0] &&
                                topUsers.get(i).getName().equals(user.getEmail()))) {
                    ref.child("Score").child("user").child(String.valueOf(i)).setValue(score);
                    indexOfRemovedElement = i;
                    Toast.makeText(PieChartActivity.this, "You're one of the best users!!!",
                            Toast.LENGTH_SHORT).show();
                    break;
                } else if (Long.parseLong(topUsers.get(i).getTime()) <= QuestionFragment.getTransitTime() ||
                        Integer.parseInt(String.valueOf(topUsers.get(i).getValue())) <= amountOfAnswers[0]) {
                    ref.child("Score").child("user").child(String.valueOf(i)).setValue(score);
                    Toast.makeText(PieChartActivity.this, "You're one of the best users!!!",
                            Toast.LENGTH_SHORT).show();
                    indexOfRemovedElement = i;
                    break;
                }
            }
            if (indexOfRemovedElement >= 0) {
                topUsers.remove(indexOfRemovedElement);
                topUsers.add(score);
            }
            sortList();
            firstUser.setText("1) " + scoreList.get(0).toString());
            secondUser.setText("2) " + scoreList.get(1).toString());
            thirdUser.setText("3)" + scoreList.get(2).toString());
        }
        if (topUsers.size() == 1) {
            sortList();
            firstUser.setText("1) " + scoreList.get(0).toString());
        }
        if (topUsers.size() == 2) {
            sortList();
            firstUser.setText("1) " + scoreList.get(0).toString());
            secondUser.setText("2) " + scoreList.get(1).toString());
        }

    }

    private void addUser() {
        int indexOfRemovedElement = -1;
        if (topUsers.size() >= 1) {
            for (int i = topUsers.size() - 1; i >= 0; i--) {
                if ((Long.parseLong(topUsers.get(i).getTime()) <= QuestionFragment.getTransitTime() &&
                        topUsers.get(i).getName().equals(user.getEmail())) ||
                        (Integer.valueOf(String.valueOf(topUsers.get(i).getValue())) <= amountOfAnswers[0] &&
                                topUsers.get(i).getName().equals(user.getEmail()))) {
                    ref.child("Score").child("user").child(String.valueOf(i)).setValue(score);
                    indexOfRemovedElement = i;
                    Toast.makeText(PieChartActivity.this, "You're one of the best users!!!",
                            Toast.LENGTH_SHORT).show();
                    break;
                } else if (Long.parseLong(topUsers.get(i).getTime()) <= QuestionFragment.getTransitTime() ||
                        Integer.parseInt(String.valueOf(topUsers.get(i).getValue())) <= amountOfAnswers[0]) {
                    ref.child("Score").child("user").child(String.valueOf(i + 1)).setValue(score);
                    Toast.makeText(PieChartActivity.this, "You're one of the best users!!!",
                            Toast.LENGTH_SHORT).show();
                    indexOfRemovedElement = i;
                    break;
                } else if (!topUsers.get(i).getName().equals(user.getEmail())) {
                    ref.child("Score").child("user").child(String.valueOf(i)).setValue(score);
                    Toast.makeText(PieChartActivity.this, "You're one of the best users!!!",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (indexOfRemovedElement >= 0) {
                topUsers.remove(indexOfRemovedElement);
            }
            topUsers.add(score);
        } else {
            ref.child("Score").child("user").child(String.valueOf(1)).setValue(score);
            topUsers.add(score);
        }
    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        if (amountOfAnswers.length == 3) {
            for (int i = 0; i < answers.length; i++) {
                dataEntries.add(new ValueDataEntry(answers[i], amountOfAnswers[i]));
            }
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    public static void setAmountOfAnswers(int[] amountOfAnswers) {
        PieChartActivity.amountOfAnswers = amountOfAnswers;
    }
}