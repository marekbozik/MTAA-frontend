package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private Button buttonToRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button userbutt =  findViewById(R.id.UserButton);
        userbutt.setText(AndroidUser.getUserName());

        findViewById(R.id.HomeToRecipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRecipes();
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