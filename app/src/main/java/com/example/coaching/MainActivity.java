package com.example.coaching;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coaching.tutorial.CompleteActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;




import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LauncherActivity launch = new LauncherActivity();
                //launch.openSampleSocketActivity(view);
                presmeruj();
            }
        });
    }





    private void presmeruj()
    {/*
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startActivity(new Intent(this, CompleteActivity.class));*/

        Intent i = new Intent(this, LauncherActivity.class);



        button.setText("Stlacene");
        startActivity(i);
        System.out.println("start recipe");


    }

}