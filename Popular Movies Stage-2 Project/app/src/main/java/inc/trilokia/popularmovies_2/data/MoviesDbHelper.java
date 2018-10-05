package inc.trilokia.popularmovies_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MoviesContract.Favourites.CREATE_FAVOURITES_TABLE);
        db.execSQL(MoviesContract.Genres.CREATE_GENRES_TABLE);
        db.execSQL(MoviesContract.MovieGenres.CREATE_MOVIE_GENRES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To be implemented when database version changes
    }

}