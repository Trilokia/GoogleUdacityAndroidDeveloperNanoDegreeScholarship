package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private final static String _TAG = JsonUtils.class.getSimpleName();
    private final static String _NAME = "name";
    private final static String _MAIN_NAME = "mainName";
    private final static String _ALSO_KNOWN_AS = "alsoKnownAs";
    private final static String _PLACE_OF_ORIGIN = "placeOfOrigin";
    private final static String _DESCRIPTION = "description";
    private final static String _IMAGE = "image";
    private final static String _INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {
        try {
            //Basic JSON Parser: Self Explanatory
            JSONObject jObj = new JSONObject(json);

            JSONObject name = jObj.getJSONObject(_NAME);

            String mainName = name.getString(_MAIN_NAME);

            JSONArray alsoKnownAs = name.getJSONArray(_ALSO_KNOWN_AS);
            List<String> alsoKnownAsList = new ArrayList<>(alsoKnownAs.length());
            for (int i = 0; i < alsoKnownAs.length(); i++) {
                alsoKnownAsList.add(alsoKnownAs.getString(i));
            }

            String placeOfOrigin = jObj.optString(_PLACE_OF_ORIGIN);

            String description = jObj.getString(_DESCRIPTION);

            String image = jObj.optString(_IMAGE);

            JSONArray ingredients = jObj.getJSONArray(_INGREDIENTS);
            List<String> ingredientsList = new ArrayList<>(ingredients.length());
            for (int i = 0; i < ingredients.length(); i++) {
                ingredientsList.add(ingredients.getString(i));
            }

            return new Sandwich(mainName, alsoKnownAsList, placeOfOrigin, description, image, ingredientsList);
        } catch (JSONException e) {
            Log.e(_TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
