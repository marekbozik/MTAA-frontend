package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

public class Activities extends AppCompatActivity {

    private Context context;

    AsyncHttpClient httpClient;
    private ArrayList<RecipeRecord> recipes;
    private ProgressBar progressBar;
    private int lastImgId = Integer.MAX_VALUE;

    private String [] spinner;

    private Button addRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        context = this;
        progressBar = findViewById(R.id.activityProgressBar);

        findViewById(R.id.AsearchMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSearch(context);
            }
        });

        findViewById(R.id.AtimelineMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toTimeline(context);
            }
        });

        findViewById(R.id.AactivityMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toActivities(context);
            }
        });

        findViewById(R.id.AhomeMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toHome(context);
            }
        });

        findViewById(R.id.ArecipesMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toRecipes(context);
            }
        });

        httpClient = new AsyncHttpClient();
        httpClient.setTimeout(120000);


        addRecipe = findViewById(R.id.addActRecord);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUser.setAddingRecord(AndroidUser.RECORD_TYPE_ACTIVITY);
                Navigator.toAddRecord(context);
            }
        });
    }
}