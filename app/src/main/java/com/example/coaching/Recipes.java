package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Recipes extends AppCompatActivity {

    private TextView recipe1;
    private String s;
    AsyncHttpClient httpClient;
    private ArrayList<RecipeRecord> recipes;
    private boolean loadDone;
    private AppCompatActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        System.out.println("recipe construct");
        context = this;

        httpClient = new AsyncHttpClient();
        loadDone = false;

        getRecipes();


    }

    private JSONArray parseJson(String response)
    {
        JSONArray jsons = null;

        try {
            jsons = new JSONArray(response);
        } catch (JSONException e) {
            return new JSONArray();
        }

        return jsons;
    }

    private RecipeRecord getRecipeById(int id)
    {
        for (RecipeRecord record: recipes) {
            if (record.getId() == id)
                return record;
        }
        return null;
    }

    private void getImages()
    {
        for(int i = 0; i < recipes.size(); i++)
        {
            String getString = "http://10.0.2.2:8000/recipe/image/" + recipes.get(i).getId();

            httpClient.get(getString, null, new BinaryHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {

                    Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                    String [] s = getString.split("/");
                    int id = Integer.parseInt(s[s.length - 1]);
                    getRecipeById(id).setImage(bitmap);

                    LinearLayout recipeLayout = findViewById(R.id.recipeLayout);
                    Button b = new Button(context);
                    b.setText(getRecipeById(id).getName());

                    Drawable image =  new BitmapDrawable(bitmap);
                    int h = image.getIntrinsicHeight();
                    int w = image.getIntrinsicWidth();
                    image.setBounds( 0, 0, w, h );

                    b.setCompoundDrawables(null, image, null, null);

                    recipeLayout.addView(b);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

                }
            });
        }
        //showRecipes();
    }


    private void showRecipes() {


        for(int i = 0; i < recipes.size(); i++)
        {
            LinearLayout recipeLayout = findViewById(R.id.recipeLayout);
            Button b = new Button(this);
            b.setText(recipes.get(i).getName());

            Drawable image = getResources().getDrawable(R.drawable.ic_baseline_home_24);
            int h = image.getIntrinsicHeight();
            int w = image.getIntrinsicWidth();
            image.setBounds( 0, 0, w, h );

            b.setCompoundDrawables(null, image, null, null);

            recipeLayout.addView(b);
        }
    }

    private void getRecipes()
    {
        JSONArray jsons = new JSONArray();
        httpClient.get("http://10.0.2.2:8000/recipes/", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONArray jsons = parseJson(responseString);
                recipes = new ArrayList<>(jsons.length());
                for (int i = 0; i < jsons.length(); i++)
                {
                    try {
                        JSONObject ji = jsons.getJSONObject(i);
                        recipes.add(new RecipeRecord(ji.getInt("id"), ji.getString("name")));
                    } catch (JSONException e) {

                    }
                }
                getImages();
            }
        });


    }


}