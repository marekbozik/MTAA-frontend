//package com.shivam.androidwebrtc;
package com.example.coaching;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

//import com.myhexaville.androidwebrtc.R;
import com.example.coaching.R;
import com.example.coaching.tutorial.CompleteActivity;
//import com.shivam.androidwebrtc.tutorial.CompleteActivity;
//import com.example.coaching

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void openSampleSocketActivity(View view) {
        startActivity(new Intent(this, CompleteActivity.class));
    }
}
