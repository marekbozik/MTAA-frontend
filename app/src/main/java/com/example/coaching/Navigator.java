package com.example.coaching;

import android.content.Context;
import android.content.Intent;

public class Navigator {

    public static void toRecipes(Context context)
    {
        Intent i = new Intent(context, Recipes.class);
        context.startActivity(i);
    }

    public static void toAddRecord(Context context)
    {
        Intent i = new Intent(context, AddRecord.class);
        context.startActivity(i);
    }

    public static void toLogin(Context context)
    {
        Intent i = new Intent(context, Login.class);
        context.startActivity(i);
    }

    public static void toRegister(Context context)
    {
        Intent i = new Intent(context, Register.class);
        context.startActivity(i);
    }

    public static void toRecipe(Context context, RecipeRecord id)
    {
        Intent i = new Intent(context, ShowRecipe.class);
        i.putExtra("Recipe", id);
        context.startActivity(i);
    }

    public static void toActivity(Context context, RecipeRecord id)
    {
        Intent i = new Intent(context, ShowRecipe.class);
        i.putExtra("Activity", id);
        context.startActivity(i);
    }


    public static void toActivities(Context context)
    {
        Intent i = new Intent(context, Activities.class);
        context.startActivity(i);
    }

    public static void toHome(Context context)
    {
        Intent i = new Intent(context, Home.class);
        context.startActivity(i);
    }

    public static void toSettings(Context context)
    {
        Intent i = new Intent(context, Settings.class);
        context.startActivity(i);
    }

    public static void toTimeline(Context context)
    {
        Intent i = new Intent(context, Timeline.class);
        context.startActivity(i);
    }

    public static void toSearch(Context context)
    {
        Intent i = new Intent(context, Search.class);
        context.startActivity(i);
    }

    public static void toChangePasswd(Context context)
    {
        Intent i = new Intent(context, ChangePasswd.class);
        context.startActivity(i);
    }

    public static void toChangeName(Context context)
    {
        Intent i = new Intent(context, ChangeName.class);
        context.startActivity(i);
    }

}
