package com.example.coaching;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    public static void createToast(Context c, String message){
        Toast toast = Toast.makeText(c, message, Toast.LENGTH_LONG);
        toast.show();
        return;
    }
}
