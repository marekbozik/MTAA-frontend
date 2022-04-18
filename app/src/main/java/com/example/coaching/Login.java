package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
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

        ProgressBar pb = findViewById(R.id.LoginProgressBar);
        pb.setIndeterminate(true);
        pb.setVisibility(View.GONE);

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
        ProgressBar pb = findViewById(R.id.LoginProgressBar);
        pb.setVisibility(View.VISIBLE);
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
                        ProgressBar pb = findViewById(R.id.LoginProgressBar);
                        pb.setVisibility(View.GONE);
                        Utils.createToast(context, "Username or password incorrect");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            JSONObject reg = new JSONObject(responseString);
                            System.out.println(reg);
                            AndroidUser.setUserId(reg.getInt("user_id"));
                            boolean userType = AndroidUser.USER_COACH;
                            if (reg.getString("user_type").equals("follower"))
                                userType = AndroidUser.USER_FOLLOWER;
                            System.out.println("user_type: " + userType);
                            AndroidUser.setUserType(userType);

                            JSONArray followings = null;

                            String param = "follower";
                            if(AndroidUser.getUserType() == AndroidUser.USER_FOLLOWER){
                                param = "coach";
                            }
                            try {
                                JSONArray arr = reg.getJSONArray("followings");
                                System.out.println("arr: " + arr);
                                for (int i = 0; i < arr.length(); i++)
                                {
                                    AndroidUser.addFollowing(arr.getJSONObject(i).getInt(param));
                                }

                            }
                            catch (JSONException e){
                                System.out.println("prazdny zoznam");
                                AndroidUser.resetFOLLOWINGS();
                            }
                            System.out.println("followings: " + AndroidUser.getFOLLOWINGS());
                            AndroidUser.setToken(reg.getString("authorization"));
                            AndroidUser.setUserName(reg.getString("user_name"));

                            Intent i = new Intent(context, Home.class);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ProgressBar pb = findViewById(R.id.LoginProgressBar);
                        pb.setVisibility(View.GONE);
                    }
                });
        //httpClient.post("http://10.0.2.2:8000/login/", )
    }
}