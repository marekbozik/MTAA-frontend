package com.example.coaching;

import android.graphics.Bitmap;

public interface Record {
    int id = 0;
    String name = "Unkonwn";
    Bitmap image = null;

    int getId();
    String getName();
    Bitmap getImage();

}
