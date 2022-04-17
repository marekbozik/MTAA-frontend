package com.example.coaching;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Timeline extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        context = this;

        ProgressBar pb = findViewById(R.id.timelineProgressBar);
        pb.setIndeterminate(true);
        findViewById(R.id.TsearchMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toSearch(context);
            }
        });

        findViewById(R.id.TtimelineMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toTimeline(context);
            }
        });

        findViewById(R.id.TactivityMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toActivities(context);
            }
        });

        findViewById(R.id.ThomeMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toHome(context);
            }
        });

        findViewById(R.id.TrecipesMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toRecipes(context);
            }
        });


        getContent();

    }

    private void changeTimelineId(int id, Calendar calendar, int val1, int val2)
    {
        System.out.println("Val1: " + val1);
        System.out.println("Val2: " + val2);

        //2020-12-18 13:11:09

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.GERMANY);
        String timestamp = calendar.get(Calendar.YEAR) + "-" +  "0"  + (calendar.get(Calendar.MONTH) + 1) + "-" +  calendar.get(Calendar.DAY_OF_MONTH) + " " +
                            val1 + ":" + val2 + ":00";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("timestamp", timestamp);
            jsonParams.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", AndroidUser.getToken());
        client.put(context, HttpHelper.getBaseAddress() + "timeline/" + AndroidUser.getUserId(), entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
                Utils.createToast(context, "Timestamp change failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Utils.createToast(context, "Timestamp changed");
                Navigator.toTimeline(context);

            }
        });

        System.out.println(timestamp);



    }

    private void getContent()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Authorization", AndroidUser.getToken());

        client.get(HttpHelper.getBaseAddress() + "timeline/" + AndroidUser.getUserId(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Timeline: " + responseString);

                ArrayList<LinearLayout> records = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(responseString);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject o = jsonArray.getJSONObject(i);
                        Button b = new Button(context);
                        b.setText(o.getString("record_name"));
                        b.setGravity(Gravity.CENTER | Gravity.LEFT);
                        if (o.getString("record_type").equals("recipe")){
                            b.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_food_bank_24),null, null, null);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Navigator.toRecipe(context, new RecipeRecord(o.getInt("record_id"), o.getString("record_name")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        else {
                            b.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_directions_run_24),null, null, null);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //TODO
                                    Utils.createToast(context, "Not implemented");
                                }
                            });
                        }

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                3.0f
                        );
                        b.setLayoutParams(param);

                        Button buttonTime = new Button(context);
                        param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                               5.0f
                        );
                        buttonTime.setLayoutParams(param);


                        Calendar calendar =  Calendar.getInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.GERMANY);
                        try {
                            calendar.setTime(sdf.parse(o.getString("timestamp")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + " " +
                                calendar.get(Calendar.YEAR) + "-" +  calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));

                        buttonTime.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_access_time_24),null, null, null);
                        buttonTime.setText(o.getString("timestamp").split("T")[1].split("[.]")[0].split("Z")[0] + " " +
                                calendar.get(Calendar.YEAR) + "-" +  (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                        //buttonTime.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + " " +
                         //       calendar.get(Calendar.YEAR) + "-" +  (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));

                        buttonTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                    try {
                                        changeTimelineId(o.getInt("id"), calendar, i, i1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 0,0 , true).show();
                        }
                    });

                        LinearLayout subL = new LinearLayout(context);
                        subL.setOrientation(LinearLayout.HORIZONTAL);
                        subL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        subL.addView(buttonTime);
                        subL.addView(b);

                        LinearLayout tll = findViewById(R.id.timelineLayout);
                        tll.addView(subL);

                        //records.add(subL);

                        //records.add(b);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                for (int i = 0; i < records.size(); i++)
                {
                    LinearLayout tll = findViewById(R.id.timelineLayout);
                    tll.addView(records.get(i));
                }*/
                ProgressBar pb = findViewById(R.id.timelineProgressBar);
                pb.setIndeterminate(false);
                pb.setVisibility(View.GONE);
            }
        });


    }
}