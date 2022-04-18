package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ChangeName extends AppCompatActivity {

    private Context context;
    private Button submitButton;
    private EditText editText;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        context = this;

        editText = findViewById(R.id.editTextNameNew);

        submitButton = findViewById(R.id.SubmitNameChangeButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpClient = new AsyncHttpClient();
                changeName();
                Navigator.toSettings(context);
            }
        });
    }

    private void changeName() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("new_name", editText.getText().toString());
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
        httpClient.put(this, HttpHelper.getBaseAddress() + "user/name/" + AndroidUser.getUserId(), entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try{
                    JSONObject reg = new JSONObject(responseString);
                    Utils.createToast(context, reg.getString("stauts"));
                } catch (JSONException e) {
                    Utils.createToast(context, "Something went wrong");
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try{
                    JSONObject reg = new JSONObject(responseString);
                    Utils.createToast(context, reg.getString("stauts"));
                    AndroidUser.setUserName(editText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}