package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ChangePasswd extends AppCompatActivity {

    private Button submitButton;
    private Context context;
    private EditText oldPasswdText;
    private EditText newPasswdText;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwd);
        context = this;

        oldPasswdText = findViewById(R.id.editTextPasswordOld);
        newPasswdText = findViewById(R.id.editTextPasswordNew);

        submitButton = findViewById(R.id.SubmitPasswdChangeButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpClient = new AsyncHttpClient();
                changePasswd();
                Navigator.toSettings(context);
            }
        });
    }

    private void changePasswd(){

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("password_old", oldPasswdText.getText().toString());
            jsonParams.put("password_new", newPasswdText.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpClient.addHeader("Authorization", AndroidUser.getToken());
        httpClient.put(this, HttpHelper.getBaseAddress() + "password/new/" + AndroidUser.getUserId(), entity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try{
                    JSONObject reg = new JSONObject(responseString);
                    //System.out.println(responseString);
                    Utils.createToast(context, reg.getString("stauts"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Utils.createToast(context, "Username or password incorrect");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try{
                    JSONObject reg = new JSONObject(responseString);
                    //System.out.println(responseString);
                    Utils.createToast(context, reg.getString("stauts"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}