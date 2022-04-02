package com.example.coaching;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                presmeruj();
            }
        });
    }





    private void presmeruj()
    {
        Intent i = new Intent(this, Recipes.class);



        button.setText("Stlacene");
        startActivity(i);
        System.out.println("start recipe");
    }

}