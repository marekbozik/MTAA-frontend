package com.example.coaching;

import android.graphics.Bitmap;

public class RecipeRecord implements Record {
    private int id;
    private String name;
    private Bitmap image;

    public RecipeRecord(int id, String name, Bitmap image)
    {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public RecipeRecord(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.image = null;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }
}
