package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private Button buttonToRecipes;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;

        Button userbutt =  findViewById(R.id.UserButton);
        userbutt.setText(AndroidUser.getUserName());

        findViewById(R.id.HomeToRecipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRecipes();
            }
        });

        findViewById(R.id.HomeToActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toActivities(context);
            }
        });

        findViewById(R.id.HomeToSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSearch(context);
            }
        });

        findViewById(R.id.HomeToTimeline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toTimeline(context);
            }
        });

        findViewById(R.id.HomeToSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSettings(context);
            }
        });

        findViewById(R.id.searchMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSearch(context);
            }
        });

        findViewById(R.id.timelineMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toTimeline(context);
            }
        });

        findViewById(R.id.activityMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toActivities(context);
            }
        });

        buttonToRecipes = findViewById(R.id.recipesMenu);
        buttonToRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRecipes();
            }
        });

    }

    private void toRecipes()
    {
        Navigator.toRecipes(this);
    }
}