package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ShowRecipe extends AppCompatActivity {

    private TextView recipeText;
    private AsyncHttpClient httpClient;
    private ImageView imageView;
    private Context context;
    private boolean isRecipe;
    private RecipeRecord rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_recipe);
        context = this;



        isRecipe = true;
        try {
            rec = (RecipeRecord) getIntent().getSerializableExtra("Recipe");
            rec.getName();
        }
        catch (Exception e)
        {
            rec = (RecipeRecord) getIntent().getSerializableExtra("Activity");
            isRecipe = false;

        }
        TextView t =  findViewById(R.id.recipeViewName);
        t.setText(rec.getName());
        recipeText =  findViewById(R.id.recipeViewText);

        imageView =  findViewById(R.id.recipeViewImage);

        httpClient = new AsyncHttpClient();

        showRecipeText(rec.getId());
        showRecipeImage(rec.getId());

        if (AndroidUser.getUserType() == AndroidUser.USER_COACH){
            findViewById(R.id.recipeAddToTimeline).setVisibility(View.GONE);
        }
        findViewById(R.id.recipeAddToTimeline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject t = new JSONObject();
                try {
                    t.put("user_id", AndroidUser.getUserId());
                    if (isRecipe)
                        t.put("recipe_id", rec.getId());
                    else
                        t.put("activity_id", rec.getId());

                }
                catch (Exception e) {
                    return;
                }

                StringEntity entity = null;
                try {
                    entity = new StringEntity(t.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String s = "recipe";
                if (!isRecipe)
                    s = "activity";
                httpClient.addHeader("Authorization", AndroidUser.getToken());
                httpClient.post(context, HttpHelper.getBaseAddress() + "timeline/" + s + "/", entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Utils.createToast(context, "Adding to timeline failed");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Utils.createToast(context, "Added to timeline");
                    }
                });
            }
        });

        //System.out.println("NOVE SCREEN:" + rec.getName());
    }

    private void showRecipeText(int id)
    {
        String s = "recipe";
        if (!isRecipe)
            s = "activity";
        httpClient.get(HttpHelper.getBaseAddress() + s + "/" + id, new TextHttpResponseHandler() {

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
        String s = "recipe";
        if (!isRecipe)
            s = "activity";
        httpClient.get(HttpHelper.getBaseAddress() + s + "/image/" + id, new BinaryHttpResponseHandler() {


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