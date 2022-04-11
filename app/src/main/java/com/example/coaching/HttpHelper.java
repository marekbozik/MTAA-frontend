package com.example.coaching;

public class HttpHelper {
    private static String BASE_ADDRESS = "http://10.0.2.2:8000/";

    public static void setBaseAddress(String address)
    {
        BASE_ADDRESS = address;
    }

    public static String getBaseAddress()
    {
        return BASE_ADDRESS;
    }
}
