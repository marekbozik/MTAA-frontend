package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ShowRecipe extends AppCompatActivity {

    private TextView recipeText;
    private AsyncHttpClient httpClient;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_recipe);
        RecipeRecord rec = (RecipeRecord) getIntent().getSerializableExtra("Recipe");
        TextView t =  findViewById(R.id.recipeViewName);
        t.setText(rec.getName());
        recipeText =  findViewById(R.id.recipeViewText);

        imageView =  findViewById(R.id.recipeViewImage);

        httpClient = new AsyncHttpClient();

        showRecipeText(rec.getId());
        showRecipeImage(rec.getId());

        //System.out.println("NOVE SCREEN:" + rec.getName());
    }

    private void showRecipeText(int id)
    {
        httpClient.get(HttpHelper.getBaseAddress() + "recipe/" + id, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject ji = new JSONObject(responseString);
                    recipeText.setText(ji.getString("text"));
                    //recipes.add(new RecipeRecord(ji.getInt("id"), ji.getString("name")));
                } catch (JSONException e) {

                }
            }
        });

    }

    private void showRecipeImage(int id)
    {
        httpClient.get(HttpHelper.getBaseAddress() + "recipe/image/" + id, new BinaryHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);

                Drawable image =  new BitmapDrawable(bitmap);
                int h = image.getIntrinsicHeight();
                int w = image.getIntrinsicWidth();
                image.setBounds( 0, 0, w, h );
                imageView.setImageDrawable(image);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

            }
        });
    }


}