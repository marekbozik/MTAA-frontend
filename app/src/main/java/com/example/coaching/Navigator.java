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
}
