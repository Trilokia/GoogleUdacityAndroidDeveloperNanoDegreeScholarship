package inc.trilokia.popularmovies_1.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static inc.trilokia.popularmovies_1.LoginActivity.API_KEY;

public final class NetworkUtils {

    //API parameter constants
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/",
            API_KEY_PARAM = "api_key",


    /*
    * TODO: (Left for stage 2) implement advance sorting
    *
    * */
    SORT_PARAM = "sort_by",
            LANGUAGE_PARAM = "language",
            ADULT_PARAM = "include_adult",
            VOTE_COUNT_PARAM = "vote_count.gte";

    //Public API parameter values
    public static final String SORT_BY_POPULARITY = "popular",
            SORT_BY_RATING = "top_rated",
            LANGUAGE_EN = "en";

    //Private API parameter values
    private static final String ADULT_INCLUDE = "false";


    /**
     * Builds a URL used to make a call to themoviedb.org's API.
     *
     * @param sortParam The desired sort order
     * @return URL used to call the API.
     */
    public static URL buildUrl(String sortParam) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortParam)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)

        //TODO: (Left for stage 2) implement advance sorting
                //.appendQueryParameter(LANGUAGE_PARAM, languageParam)
                //.appendQueryParameter(ADULT_PARAM, ADULT_INCLUDE)
                //.appendQueryParameter(VOTE_COUNT_PARAM, "500")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("URL", url.toString());
        return url;
    }

    /**
     * Opens a URL connection and retrieves a String response from the API.
     *
     * @param url The URL used to access the API
     * @return Raw JSON data as a String
     * @throws IOException Standard exception.
     */
    public static String getResponseFromServer(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String response;

        try {
            connection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = streamToString(in);

            return response;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Converts InputStream data to a String.
     *
     * @param in InputStream object containing raw JSON data
     * @return Raw JSON data as String.
     */
    private static String streamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

}
