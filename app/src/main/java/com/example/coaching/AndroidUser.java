package com.example.coaching;

import org.json.JSONArray;

import java.util.ArrayList;

public class AndroidUser {

    public static boolean USER_FOLLOWER = false;
    public static boolean USER_COACH = true;
    public static String RECORD_TYPE_RECIPE = "recipe";
    public static String RECORD_TYPE_ACTIVITY = "activity";


    private static boolean isMyTimeline = true;
    private static int timelineId = 0;
    private static String ADDING_RECORD = "recipe";
    private static String TOKEN;
    private static String USERNAME;
    private static int USER_ID;
    private static boolean USER_TYPE;
    private static ArrayList<Integer> FOLLOWINGS = new ArrayList<Integer>();
    private static ArrayList<Integer> SEARCHRESULTS = new ArrayList<Integer>();

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

    public static boolean getUserType() { return USER_TYPE; }

    public static void addFollowing(int userId){ FOLLOWINGS.add(userId); }

    public static void resetFOLLOWINGS(){ FOLLOWINGS = new ArrayList<Integer>(); }

    public static void removeFollowing(int userId){ FOLLOWINGS.remove(userId); }

    public static ArrayList<Integer> getFOLLOWINGS() {
        return FOLLOWINGS;
    }

    public static String getAddingRecord()
    {
        return ADDING_RECORD;
    }

    public static void setAddingRecord(String RECORD_TYPE)
    {
        ADDING_RECORD = RECORD_TYPE;
    }


    public static String getUserName() {
        return USERNAME;
    }

    public static void setUserName(String UserName) {
        AndroidUser.USERNAME = UserName;
    }

    public static ArrayList<Integer> getSearchResult(){ return SEARCHRESULTS; }

    public static void setSearchResult(int userId){ SEARCHRESULTS.add(userId); }

    public static void resetSearchResult(){ SEARCHRESULTS.clear(); }

    public static boolean isIsMyTimeline() {
        return isMyTimeline;
    }

    public static void setIsMyTimeline(boolean isMyTimeline) {
        AndroidUser.isMyTimeline = isMyTimeline;
    }

    public static void setTimelineId(int timelineId) {
        AndroidUser.timelineId = timelineId;
    }

    public static int getTimelineId() {
        return timelineId;
    }
}
