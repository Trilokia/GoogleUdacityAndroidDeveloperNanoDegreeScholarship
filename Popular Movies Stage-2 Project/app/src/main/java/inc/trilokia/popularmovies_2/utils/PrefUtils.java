package inc.trilokia.popularmovies_2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public  class PrefUtils {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    public static String API_KEY = "MyKey";
    // Shared preferences file name
    private static final String PREF_NAME = "inc.trilokia.popularmovies_2_preferences";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    public PrefUtils(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void setApiKey(String string) {
        editor.putString(API_KEY, string);
        editor.apply();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

   public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getApiKey() {
        //returning default my api
        return pref.getString(API_KEY, "Your Key");
    }
    public String getUserName() {
        //returning default my api
        return pref.getString("MyUserName", "Guest");
    }

}
