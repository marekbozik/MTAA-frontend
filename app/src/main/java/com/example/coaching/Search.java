package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.coaching.tutorial.CompleteActivity;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Search extends AppCompatActivity {

    private Context context;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;

        searchText = findViewById(R.id.editTextSearchUser);

        findViewById(R.id.SsearchMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSearch(context);
            }
        });

        findViewById(R.id.StimelineMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toTimeline(context);
            }
        });

        findViewById(R.id.SactivityMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toActivities(context);
            }
        });

        findViewById(R.id.ShomeMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toHome(context);
            }
        });

        findViewById(R.id.SrecipesMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toRecipes(context);
            }
        });

        updateFollowings();
        getContent();

    }

    private void updateFollowings(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setTimeout(60000);

        httpClient.addHeader("Authorization", AndroidUser.getToken());
        httpClient.get(this, HttpHelper.getBaseAddress() + "followings/", null, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Zlyhalo to");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Podarilo sa");
                //Utils.createToast(context, "Unwollow completed");

                try {
                    JSONArray jsonArray = new JSONArray(responseString);
                    AndroidUser.resetFOLLOWINGS();

                    System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject reg = jsonArray.getJSONObject(i);
                        System.out.println("GET INT: " + reg.getInt("coach"));
                        AndroidUser.addFollowing(reg.getInt("coach"));
                    }
                } catch (JSONException e) {
                    System.out.println("nefunguje to");
                    e.printStackTrace();
                }

            }
        });
    }


    private void performSearch() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setTimeout(100000);

        String searchURL = "follower/";
        if(AndroidUser.getUserType() == AndroidUser.USER_FOLLOWER){
            searchURL = "coach/";
        }

        httpClient.addHeader("Authorization", AndroidUser.getToken());
        httpClient.get(HttpHelper.getBaseAddress() + searchURL + searchText.getText().toString(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Zlyhalo to");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //////
            }
        });
    }


    private void addFollowing(int userToAdd, int userId) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setTimeout(100000);

        String searchURL = "follow/follower/";
        if(AndroidUser.getUserType() == AndroidUser.USER_FOLLOWER){
            searchURL = "follow/coach/";
        }

        httpClient.addHeader("Authorization", AndroidUser.getToken());
        httpClient.post(this, HttpHelper.getBaseAddress() + searchURL + userToAdd, null, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Zlyhalo to");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Podarilo sa");
                //AndroidUser.addFollowing(userToAdd);
                Utils.createToast(context, "Unwollow completed");
                //getContent();
                Navigator.toSearch(context);
            }
        });

    }


    private void deleteFollowing(int userToDelete, int userId) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setTimeout(100000);

        String searchURL = "follow/follower/";
        if(AndroidUser.getUserType() == AndroidUser.USER_FOLLOWER){
            searchURL = "follow/coach/";
        }

        httpClient.addHeader("Authorization", AndroidUser.getToken());
        httpClient.delete(this, HttpHelper.getBaseAddress() + searchURL + userToDelete, null, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Zlyhalo to");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Podarilo sa");
                //AndroidUser.removeFollowing(userToDelete);
                //System.out.println("vymazal som? : " + AndroidUser.getFOLLOWINGS());
                Utils.createToast(context, "Unwollow completed");
                //getContent();
                Navigator.toSearch(context);
            }
        });

    }

    private void presmerujWebRTC(){
        //Intent i = new Intent(this, LauncherActivity.class);
        //startActivity(i);
        startActivity(new Intent(this, CompleteActivity.class));
    }


    private void getContent(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(100000);
        client.addHeader("Authorization", AndroidUser.getToken());

        String searchURL = "followers/";
        if(AndroidUser.getUserType() == AndroidUser.USER_FOLLOWER){
            searchURL = "coaches/";
        }

        System.out.println("userType:" + searchURL);

        client.get(HttpHelper.getBaseAddress() + searchURL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Zlyhalo to");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONArray jsonArray = new JSONArray(responseString);

                    Button searchButton = findViewById(R.id.searchNameButton);

                    searchButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            performSearch();
                        }
                    });

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);

                        Button b = new Button(context);
                        b.setText(o.getString("name"));
                        b.setGravity(Gravity.CENTER | Gravity.LEFT);

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                2.0f
                        );
                        b.setLayoutParams(param);

                        LinearLayout subL = new LinearLayout(context);
                        if(AndroidUser.getFOLLOWINGS().contains(o.getInt("id"))){
                            System.out.println("user " + o.getInt("id") + " in folllowings");

                            MaterialButton callButton = new MaterialButton(context);
                            callButton.setIcon(getDrawable(R.drawable.ic_baseline_call_24));
                            callButton.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
                            callButton.setBackgroundColor(Color.GREEN);
                            callButton.setGravity(Gravity.CENTER);

                            param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    5.0f
                            );
                            callButton.setLayoutParams(param);

                            subL.setOrientation(LinearLayout.HORIZONTAL);
                            subL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            subL.addView(b);

                            callButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    presmerujWebRTC();
                                }
                            });

                            MaterialButton banButton = new MaterialButton(context);
                            banButton.setIcon(getDrawable(R.drawable.ic_baseline_remove_24));
                            banButton.setGravity(Gravity.CENTER);
                            banButton.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
                            banButton.setBackgroundColor(Color.RED);
                            banButton.setLayoutParams(param);

                            banButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        deleteFollowing(o.getInt("id"), AndroidUser.getUserId());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            subL.addView(callButton);
                            subL.addView(banButton);
                        }
                        else {
                            MaterialButton addButton = new MaterialButton(context);
                            addButton.setIcon(getDrawable(R.drawable.ic_baseline_add_24));
                            addButton.setGravity(Gravity.CENTER);
                            param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    7.0f
                            );
                            addButton.setLayoutParams(param);

                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        addFollowing(o.getInt("id"), AndroidUser.getUserId());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            subL.setOrientation(LinearLayout.HORIZONTAL);
                            subL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            subL.addView(b);
                            subL.addView(addButton);
                        }
                        LinearLayout tll = findViewById(R.id.searchLayout);
                        tll.addView(subL);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}