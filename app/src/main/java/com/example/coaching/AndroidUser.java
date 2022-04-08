package com.example.coaching;

import java.util.ArrayList;

public class AndroidUser {

    public static boolean USER_FOLLOWER = false;
    public static boolean USER_COACH = true;

    private static String TOKEN;
    private static int USER_ID;
    private static boolean USER_TYPE;
    private static ArrayList<Integer> FOLLOWINGS = new ArrayList<>();

    public static void setToken(String token)
    {
        TOKEN = token;
    }

    public static String getToken()
    {
        return TOKEN;
    }

    public static int getUserId() {
        return USER_ID;
    }

    public static void setUserId(int userId) {
        USER_ID = userId;
    }

    public static boolean isUserType() {
        return USER_TYPE;
    }

    public static void setUserType(boolean userType) {
        USER_TYPE = userType;
    }

    public static ArrayList<Integer> getFOLLOWINGS() {
        return FOLLOWINGS;
    }

}
