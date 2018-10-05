package inc.trilokia.popularmovies_2.utils;

import android.net.Uri;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class StringFormattingUtils {

    public static String buildImagePath(String imageSize, String imagePath) {
        return String.format("http://image.tmdb.org/t/p/%s/%s", imageSize, imagePath);
    }

    public static String formatReleaseDate(String releaseDate) {
        DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date sourceDate = sourceFormat.parse(releaseDate);
            DateFormat desiredFormat = DateFormat.getDateInstance(DateFormat.LONG);

            return desiredFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();

            return "Release date unknown";
        }
    }

    public static Uri buildTrailerPath(Object trailerKey) {
        return Uri.parse("https://www.youtube.com/watch?v=" + trailerKey);
    }

    public static String buildGenresString(List<String> genreNames) {
        String genresString = "";

        for (int i = 0; i < genreNames.size(); i++) {
            genresString += genreNames.get(i);
            if (i != genreNames.size() - 1) {
                genresString += "/";
            }
        }

        return genresString;
    }

}