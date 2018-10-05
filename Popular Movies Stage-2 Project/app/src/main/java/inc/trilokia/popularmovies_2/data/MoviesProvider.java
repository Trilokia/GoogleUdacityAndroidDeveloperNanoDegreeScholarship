package inc.trilokia.popularmovies_2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesProvider extends ContentProvider {

    public static final int FAVOURITES = 100;
    public static final int FAVOURITE_WITH_MOVIE_ID = 101;
    public static final int GENRES = 200;
    public static final int GENRE_WITH_GENRE_ID = 201;
    public static final int MOVIE_GENRES = 300;
    public static final int MOVIE_GENRES_WITH_MOVIE_ID = 301;

    private static final String FAVOURITES_TABLE_NAME = MoviesContract.Favourites.TABLE_NAME;
    private static final String GENRES_TABLE_NAME = MoviesContract.Genres.TABLE_NAME;
    private static final String MOVIE_GENRES_TABLE_NAME = MoviesContract.MovieGenres.TABLE_NAME;

    private MoviesDbHelper mDbHelper;
    private static final UriMatcher sMatcher = buildUriMatcher();

    private static final String FAILED_INSERT = "Failed to insert row into ";
    private static final String UNKNOWN_URI = "Unknown URI: ";

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVOURITES, FAVOURITES);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVOURITES + "/#",
                FAVOURITE_WITH_MOVIE_ID);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_GENRES, GENRES);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_GENRES + "/#",
                GENRE_WITH_GENRE_ID);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIE_GENRES, MOVIE_GENRES);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIE_GENRES + "/#",
                MOVIE_GENRES_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sMatcher.match(uri);
        long id;
        Uri returnUri;

        switch (match) {
            case FAVOURITES:
                id = db.insert(FAVOURITES_TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(
                            MoviesContract.Favourites.CONTENT_URI, id);
                } else {
                    throw new SQLException(FAILED_INSERT + uri);
                }
                break;

            case GENRES:
                id = db.insert(GENRES_TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.Genres.CONTENT_URI, id);
                } else {
                    throw new SQLException(FAILED_INSERT + uri);
                }
                break;

            case MOVIE_GENRES:
                id = db.insert(MOVIE_GENRES_TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MovieGenres.CONTENT_URI, id);
                } else {
                    throw new SQLException(FAILED_INSERT + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sMatcher.match(uri);
        Cursor returnCursor;
        String movieId;

        switch (match) {
            case FAVOURITES:
                returnCursor = db.query(FAVOURITES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVOURITE_WITH_MOVIE_ID:
                movieId = uri.getPathSegments().get(1);

                returnCursor = db.query(FAVOURITES_TABLE_NAME,
                        projection,
                        "movieId=?",
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder);
                break;

            case GENRES:
                returnCursor = db.query(GENRES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case GENRE_WITH_GENRE_ID:
                String genreId = uri.getPathSegments().get(1);

                returnCursor = db.query(GENRES_TABLE_NAME,
                        projection,
                        "genreId=?",
                        new String[]{genreId},
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIE_GENRES_WITH_MOVIE_ID:
                movieId = uri.getPathSegments().get(1);

                returnCursor = db.query(MOVIE_GENRES_TABLE_NAME,
                        projection,
                        "movieId=?",
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder);

                //returnCursor.close();
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sMatcher.match(uri);
        String movieId;
        int rowsAffected;

        switch (match) {
            case FAVOURITE_WITH_MOVIE_ID:
                movieId = uri.getPathSegments().get(1);

                rowsAffected = db.delete(MoviesContract.Favourites.TABLE_NAME,
                        "movieId=?",
                        new String[]{movieId});
                break;

            case MOVIE_GENRES_WITH_MOVIE_ID:
                movieId = uri.getPathSegments().get(1);

                rowsAffected = db.delete(MoviesContract.MovieGenres.TABLE_NAME,
                        "movieId=?",
                        new String[]{movieId});
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        if (rowsAffected != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsAffected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

}