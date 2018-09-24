package inc.trilokia.popularmovies_1.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//TODO:(For Stage 2) Consider making Parcelable
public class Movie implements Serializable {

    //Movie variables
    private String mTitle, mPosterPath, mOverview, mReleaseDate;
    private Number mVoteAverage;

    //Poster size constants
    public static final String POSTER_SMALL = "w342", POSTER_MEDIUM = "w500",POSTER_LARGE = "w780";

    /**
     * Movie title getter.
     *
     * @return The movie's title as a String.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Constructs a URL for the movie's poster.
     *
     * @param posterSize The desired poster size
     * @return The full poster path.
     */
    public String getPosterPath(String posterSize) {
        return String.format("http://image.tmdb.org/t/p/%s/%s", posterSize, mPosterPath);
    }

    /**
     * Movie overview getter.
     *
     * @return The movie's overview as a String.
     */
    public String getOverview() {
        return mOverview;
    }

    /**
     * Converts the source release date to a custom formatted date.
     *
     * @return The movie's release date in DateFormat.LONG as a String.
     */
    public String getReleaseDate() {
        DateFormat sourceFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date desiredFormat;

        try {
            desiredFormat = sourceFormat.parse(mReleaseDate);
            DateFormat desiredFormatter = DateFormat.getDateInstance(DateFormat.LONG);

            return desiredFormatter.format(desiredFormat).toString();
        } catch (ParseException e) {
            e.printStackTrace();

            return "Unknown";
        }
    }

    /**
     * VoteAverage getter.
     *
     * @return The movie's VoteAverage as a Number.
     */
    public Number getVoteAverage() {
        return mVoteAverage;
    }

    /**
     * Movie constructor.
     *
     * @param title Movie title stored as String
     * @param posterPath Partial movie poster path stored as String
     * @param overview Movie overview stored as String
     * @param releaseDate Movie release date stored as String
     * @param voteAverage Movie vote average stored as Number.
     */
    public Movie(String title, String posterPath, String overview, String releaseDate,
                 Number voteAverage) {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

}
