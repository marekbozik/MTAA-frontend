package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    private Button changeNameButton;
    private Button changePasswdButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;

        findViewById(R.id.SettingssearchMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSearch(context);
            }
        });

        findViewById(R.id.SettingstimelineMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toTimeline(context);
            }
        });

        findViewById(R.id.SettingsactivityMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toActivities(context);
            }
        });

        findViewById(R.id.SettingshomeMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toHome(context);
            }
        });

        findViewById(R.id.SettingsrecipesMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toRecipes(context);
            }
        });

        changePasswdButton = findViewById(R.id.changePasswdButton);
        changePasswdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toChangePasswd(context);
            }
        });

        changeNameButton = findViewById(R.id.changeNameButton);
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toChangeName(context);
            }
        });

    }

}