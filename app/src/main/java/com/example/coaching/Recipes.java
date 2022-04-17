package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private ProgressBar progressBar;
    private int lastImgId = Integer.MAX_VALUE;

    private String [] spinner;

    private Button addRecipe;
    private AppCompatActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);


        System.out.println("recipe construct");
        context = this;
        progressBar = findViewById(R.id.recipesProgressBar);


        findViewById(R.id.RsearchMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSearch(context);
            }
        });

        findViewById(R.id.RtimelineMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toTimeline(context);
            }
        });

        findViewById(R.id.RactivityMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toActivities(context);
            }
        });

        findViewById(R.id.RhomeMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toHome(context);
            }
        });




        httpClient = new AsyncHttpClient();
        httpClient.setTimeout(120000);


        addRecipe = findViewById(R.id.addRecord);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUser.setAddingRecord(AndroidUser.RECORD_TYPE_RECIPE);
                Navigator.toAddRecord(context);
            }
        });

        //getRecipes();

        getCategories();

        Spinner s = findViewById(R.id.recipeSpinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);

                LinearLayout recipeLayout = findViewById(R.id.recipeLayout);
                recipeLayout.removeAllViews();

                if (spinner[i].equals("All"))
                {
                    getRecipes();
                }
                else
                {
                    getRecipes(spinner[i]);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



    }

    private void getCategories()
    {

        httpClient.get(HttpHelper.getBaseAddress() + "recipe/categories", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("CHYBA: "  + responseString);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                System.out.println(responseString);
                JSONArray jsons = parseJson(responseString);
                String[] arraySpinner = new String[jsons.length() + 1];
                arraySpinner[0] = "All";
                recipes = new ArrayList<>(jsons.length());
                for (int i = 0; i < jsons.length(); i++)
                {
                    try {
                        JSONObject ji = jsons.getJSONObject(i);
                        arraySpinner[i + 1] = ji.getString("category");
                    } catch (JSONException e) {

                    }
                }
                spinner = arraySpinner;
                Spinner s = findViewById(R.id.recipeSpinner);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            }
        });
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

    private void navigateToRecipe(int id)
    {
        Intent i = new Intent(context, ShowRecipe.class);
        i.putExtra("Recipe", getRecipeById(id));
        context.startActivity(i);
    }

    private void getImages()
    {
        lastImgId = recipes.get(recipes.size()-1).getId();
        for(int i = 0; i < recipes.size(); i++)
        {
            String getString = HttpHelper.getBaseAddress() + "recipe/image/" + recipes.get(i).getId();

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
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            navigateToRecipe(id);
                        }
                    });

                    Drawable image =  new BitmapDrawable(bitmap);
                    int h = image.getIntrinsicHeight();
                    int w = image.getIntrinsicWidth();
                    image.setBounds( 0, 0, w, h );

                    b.setCompoundDrawables(null, image, null, null);

                    recipeLayout.addView(b);

                    if (id == lastImgId){
                        progressBar.setIndeterminate(false);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

                }
            });
        }

    }


    private void getRecipes(String category) {
        JSONArray jsons = new JSONArray();
        httpClient.get(HttpHelper.getBaseAddress() + "/recipes/" + category, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONArray jsons = parseJson(responseString);
                recipes = new ArrayList<>(jsons.length());
                for (int i = 0; i < jsons.length(); i++) {
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

    private void getRecipes()
    {
        JSONArray jsons = new JSONArray();
        httpClient.get(HttpHelper.getBaseAddress() + "recipes/", new TextHttpResponseHandler() {
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