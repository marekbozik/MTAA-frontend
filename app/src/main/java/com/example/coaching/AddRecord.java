package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.AnnotationFormatError;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AddRecord extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private Button uploadImageButton;
    private Button createNewRecord;
    private TextView recName;
    private TextView recCategory;
    private TextView recDescription;
    private AsyncHttpClient httpClient;

    private boolean isImageSend;
    private boolean isTextSend;

    private Context context;


    private ImageView uploadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        isImageSend = false;
        isTextSend = false;
        context = this;
        httpClient = new AsyncHttpClient();

        recName = findViewById(R.id.recordNameEditText);
        recCategory = findViewById(R.id.recordCategoryEditText);
        recDescription = findViewById(R.id.recordDescriptionEditText);

        createNewRecord = findViewById(R.id.createNewRecordButton);
        createNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRecord();
            }
        });


        String [] spinner = {"Recipe", "Activity"};
        Spinner s = findViewById(R.id.addSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        uploadedImage = findViewById(R.id.uploadedImage);
        uploadImageButton =  findViewById(R.id.uploadImageButton);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            uploadedImage.setImageURI(imageUri);
        }
    }

    private boolean isValidString(String s)
    {
        if(s != null)
        {
            if (s.length() > 0){
                return true;
            }
        }
        return false;
    }

    private void createToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
        return;
    }

    /**
     * source: https://stackoverflow.com/a/29091591/15455772
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final int width = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().height() : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width,
                height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    private void createNewRecord()
    {

        String name = recName.getText().toString();
        if (!isValidString(name)) {
            createToast("Record name is not valid");
            return;
        }
        String category = recCategory.getText().toString();
        if (!isValidString(category)) {
            createToast("Record category is not valid");
            return;
        }
        String description = recDescription.getText().toString();
        if (!isValidString(description)) {
            createToast("Record description is not valid");
            return;
        }
        if (imageUri == null){
            createToast("You must choose record image");
            return;
        }



        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", name);
            jsonParams.put("text", description);
            jsonParams.put("category", category);
            jsonParams.put("user_id", AndroidUser.getUserId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(entity.getContent().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.createToast(this, "Creating new record...");

        httpClient.addHeader("Authorization", AndroidUser.getToken());
        httpClient.post(this, HttpHelper.getBaseAddress() + "recipe/", entity, "application/json",
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        isTextSend = true;
                        if (isTextSend && isImageSend)
                        {
                            Navigator.toRecipes(context);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                        Utils.createToast(context, "Uploading image...");

                        System.out.println("RESPONSE: " + responseString);
                        JSONObject jsonId = null;
                        try {
                            jsonId = new JSONObject(responseString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int id = 0;

                        try {
                            id = jsonId.getInt("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Bitmap bit = drawableToBitmap(uploadedImage.getDrawable());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        HttpEntity imageEntity = new ByteArrayEntity(byteArray);

                        httpClient.post(context, HttpHelper.getBaseAddress() + "recipe/image/" + id, imageEntity, "application/octet-stream",
                                new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        System.out.println("OBRAZOK NEPOSLANY " + statusCode);
                                        Navigator.toRecipes(context);
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                            Navigator.toRecipes(context);
                                    }
                                });

                    }
                });
    }
}