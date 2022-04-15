package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonStreamerEntity;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Login extends AppCompatActivity {
    private Button registerButton;
    private Button loginButton;
    private EditText emailEdit;
    private EditText emailPassword;
    private Context context;

    private AsyncHttpClient httpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.editTextTextEmailAddress);
        emailPassword = findViewById(R.id.editTextTextPassword);
        context = this;

        httpClient = new AsyncHttpClient();

        registerButton = findViewById(R.id.registerLoginButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toRegister(context);
            }
        });
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login(){

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("email", emailEdit.getText().toString());
            jsonParams.put("password", emailPassword.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpClient.post(this, HttpHelper.getBaseAddress() + "login/", entity, "application/json",
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("REGISTRATION ERROR");
                        Utils.createToast(context, "Username or password incorrect");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            JSONObject reg = new JSONObject(responseString);
                            AndroidUser.setUserId(reg.getInt("user_id"));
                            boolean userType = AndroidUser.USER_COACH;
                            if (reg.getString("user_type").equals("follower"))
                                userType = AndroidUser.USER_FOLLOWER;
                            AndroidUser.setUserType(userType);
                            AndroidUser.setToken(reg.getString("authorization"));
                            AndroidUser.setUserName(reg.getString("user_name"));

                            Intent i = new Intent(context, Home.class);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        //httpClient.post("http://10.0.2.2:8000/login/", )
    }
}