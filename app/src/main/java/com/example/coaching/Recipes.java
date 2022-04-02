package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Recipes extends AppCompatActivity {

    private TextView recipe1;
    private String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        recipe1 = findViewById(R.id.recipe1);
        System.out.println("recipe construct");



        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.2.2:8000/recipes/", null, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("daka chyba");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(responseString);
                s = jsonHandler(responseString);

            }
        });


        getImage();
        if (s != null)
        {
            TextView t = findViewById(R.id.recipe1);
            t.setText(s);
            System.out.println("S OK");
        }
        else
            System.out.println("S NULL");
    }


    private byte[] rawImg;
    private void getImage()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.2.2:8000/recipe/image/1", null, new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                System.out.println("OBRAZOK OK");
                ImageView imgV = findViewById(R.id.image1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                imgV.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

                System.out.println(error.getMessage() + "\n OBRAZOK FAIL " + statusCode);
                rawImg = binaryData;

                ImageView imgV = findViewById(R.id.image1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                imgV.setImageBitmap(bitmap);

            }
        });

    }

    private String jsonHandler(String s)
    {
        JSONArray jarr = null;
        try {
            jarr = new JSONArray(s);
            System.out.println("PARSE OK");
        } catch (JSONException e) {
            System.out.println("CHYBA parsovania");
            return null;
        }

        for (int i=0; i < jarr.length(); i++)
        {
            try {
                JSONObject oneObject = jarr.getJSONObject(i);
                // Pulling items from the array
                String name = oneObject.getString("name");
                int id = oneObject.getInt("id");

                TextView t = findViewById(R.id.recipe1);
                t.setText("NAME: " + name);
                return "NAME: " + name + ", ID: " + id;
            } catch (JSONException e) {
                System.out.println("CHYBA ITEROVANIA PARSE");
            }
        }
        return null;

    }

    public TextView getRecipe1()
    {
        return recipe1;
    }

}