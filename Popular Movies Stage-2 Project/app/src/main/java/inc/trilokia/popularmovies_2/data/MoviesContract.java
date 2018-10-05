package inc.trilokia.popularmovies_2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MoviesContract {

    static final String AUTHORITY = "inc.trilokia.popularmovies_2";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    static final String PATH_FAVOURITES = "favourites";
    static final String PATH_GENRES = "genres";
    static final String PATH_MOVIE_GENRES = "movie_genres";

    private MoviesContract() {
    }

    public static final class Favourites implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        static final String TABLE_NAME = "favourites";

        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";

        static final String CREATE_FAVOURITES_TABLE = "CREATE TABLE " +
                Favourites.TABLE_NAME + " (" +
                Favourites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Favourites.COLUMN_POSTER_PATH + " TEXT, " +
                Favourites.COLUMN_OVERVIEW + " TEXT, " +
                Favourites.COLUMN_RELEASE_DATE + " TEXT, " +
                Favourites.COLUMN_MOVIE_ID + " INTEGER, " +
                Favourites.COLUMN_TITLE + " TEXT NOT NULL, " +
                Favourites.COLUMN_BACKDROP_PATH + " TEXT, " +
                Favourites.COLUMN_VOTE_AVERAGE + " REAL" +
                ");";
    }

    public static final class Genres implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRES).build();

        static final String TABLE_NAME = "genres";

        public static final String COLUMN_GENRE_ID = "genreId";
        public static final String COLUMN_GENRE_NAME = "genreName";

        static final String CREATE_GENRES_TABLE = "CREATE TABLE " +
                Genres.TABLE_NAME + " (" +
                Genres._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Genres.COLUMN_GENRE_ID + " INTEGER, " +
                Genres.COLUMN_GENRE_NAME + " TEXT" +
                ");";
    }

    public static final class MovieGenres implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_GENRES).build();

        static final String TABLE_NAME = "movie_genres";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_GENRE_ID = "genreId";

        private interface References {
            String MOVIE_ID = "REFERENCES " +
                    Favourites.TABLE_NAME + "(" +
                    Favourites.COLUMN_MOVIE_ID + ")";
            String GENRE_ID = "REFERENCES " +
                    Genres.TABLE_NAME + "(" +
                    Genres.COLUMN_GENRE_ID + ")";
        }

        static final String CREATE_MOVIE_GENRES_TABLE = "CREATE TABLE " +
                MovieGenres.TABLE_NAME + " (" +
                MovieGenres._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieGenres.COLUMN_MOVIE_ID + " INTEGER " + References.MOVIE_ID + ", " +
                MovieGenres.COLUMN_GENRE_ID + " INTEGER " + References.GENRE_ID + ", " +
                "UNIQUE (" + MovieGenres.COLUMN_MOVIE_ID + ", " + MovieGenres.COLUMN_GENRE_ID + ") " +
                "ON CONFLICT REPLACE);";
    }

}