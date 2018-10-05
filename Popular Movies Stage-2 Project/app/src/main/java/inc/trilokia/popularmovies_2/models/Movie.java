package inc.trilokia.popularmovies_2.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//TODO: Make Parcelable
public class Movie implements Serializable {

    //Movie Variables
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private int[] genreIds;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_average")
    private Number voteAverage;

    //Poster and Backdrop Size Constants
    public static final String POSTER_MEDIUM = "w500";
    public static final String POSTER_LARGE = "w780";
    public static final String BACKDROP_LARGE = "w780";

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Number getVoteAverage() {
        return voteAverage;
    }

    public Movie(String posterPath, String overview, String releaseDate, int[] genreIds, int id,
                 String title, String backdropPath, Number voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

}