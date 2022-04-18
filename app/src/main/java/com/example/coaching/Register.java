package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Register extends AppCompatActivity {

    private Button registerButton;
    private EditText  textName;
    private EditText textEmail;
    private EditText textPassword;
    private CheckBox isCoach;
    private Context context;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        System.out.println("som tu");
        textName = findViewById(R.id.editTextName);
        textEmail = findViewById(R.id.editTextEmail);
        textPassword = findViewById(R.id.editTextPassword);
        isCoach = findViewById(R.id.checkboxIsCoach);
        context = this;

        httpClient = new AsyncHttpClient();
        System.out.println("holy pele" + isCoach.isChecked());
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { register(isCoach.isChecked()); } });
    }

    private void register(boolean isUserCoach){

        JSONObject jsonParams = new JSONObject();
        /*{
            "name": "Adam Blahovic",
            "email": "adam@blahovic.com",
            "password": "password",
            "is_coach": false
        }*/

        try{
            jsonParams.put("name", textName.getText().toString());
            jsonParams.put("email", textEmail.getText().toString());
            jsonParams.put("password", textPassword.getText().toString());
            jsonParams.put("is_coach", isUserCoach);
        } catch (JSONException e){
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpClient.post(this, HttpHelper.getBaseAddress() + "registration/", entity, "application/json",
            new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("REGISTRATION ERROR");
                Utils.createToast(context, "User with this email already exists");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Utils.createToast(context, "Registration successful");
                    Navigator.toLogin(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
}