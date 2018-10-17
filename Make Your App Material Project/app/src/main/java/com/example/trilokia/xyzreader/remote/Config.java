package com.example.trilokia.xyzreader.remote;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static String LOG_TAG = Config.class.getSimpleName();
    public static final URL BASE_URL;

    static {
        URL url = null;
        try {
            url = new URL("https://go.udacity.com/xyz-reader-json");
        } catch (MalformedURLException ignored) {
            Log.w(LOG_TAG, ignored.getMessage(), ignored);
        }

        BASE_URL = url;
    }
}
