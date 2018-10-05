package inc.trilokia.popularmovies_2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import inc.trilokia.popularmovies_2.data.MoviesContract;
import inc.trilokia.popularmovies_2.models.Genre;
import inc.trilokia.popularmovies_2.models.Movie;

public final class DatabaseUtils {

    public static void insertGenres(List<Genre> genres, Context context) {
        for (Genre genre : genres) {
            ContentValues values = new ContentValues();
            values.put(MoviesContract.Genres.COLUMN_GENRE_ID, genre.getId());
            values.put(MoviesContract.Genres.COLUMN_GENRE_NAME, genre.getName());
            context.getContentResolver().insert(MoviesContract.Genres.CONTENT_URI, values);
        }
    }

    public static boolean toggleFavourite(Movie movie, Context context) {
        boolean removed;

        if (movieIsFavourite(movie.getId(), context)) {
            removeFromFavourites(movie.getId(), context);
            removed = true;
        } else {
            addToFavourites(movie, context);
            removed = false;
        }

        return removed;
    }

    private static void removeFromFavourites(int movieId, Context context) {
        String id = Integer.toString(movieId);

        Uri uri = MoviesContract.Favourites.CONTENT_URI.buildUpon().appendPath(id).build();
        context.getContentResolver().delete(uri, null, null);

        uri = MoviesContract.MovieGenres.CONTENT_URI.buildUpon().appendPath(id).build();
        context.getContentResolver().delete(uri, null, null);
    }

    private static void addToFavourites(Movie movie, Context context) {
        ContentValues values = new ContentValues();

        values.put(MoviesContract.Favourites.COLUMN_POSTER_PATH,
                movie.getPosterPath());
        values.put(MoviesContract.Favourites.COLUMN_OVERVIEW,
                movie.getOverview());
        values.put(MoviesContract.Favourites.COLUMN_RELEASE_DATE,
                movie.getReleaseDate());
        values.put(MoviesContract.Favourites.COLUMN_MOVIE_ID,
                movie.getId());
        values.put(MoviesContract.Favourites.COLUMN_TITLE,
                movie.getTitle());
        values.put(MoviesContract.Favourites.COLUMN_BACKDROP_PATH,
                movie.getBackdropPath());
        values.put(MoviesContract.Favourites.COLUMN_VOTE_AVERAGE,
                movie.getVoteAverage().toString());

        context.getContentResolver().insert(MoviesContract.Favourites.CONTENT_URI, values);
        values.clear();

        for (int id : movie.getGenreIds()) {
            values = new ContentValues();
            values.put(MoviesContract.MovieGenres.COLUMN_MOVIE_ID, movie.getId());
            values.put(MoviesContract.Genres.COLUMN_GENRE_ID, id);
            context.getContentResolver().insert(MoviesContract.MovieGenres.CONTENT_URI, values);
        }
    }

    public static List<Movie> getFavourites(Context context, String sortParam) {
        Cursor movieCursor = context.getContentResolver().query(
                MoviesContract.Favourites.CONTENT_URI,
                null,
                null,
                null,
                sortParam);
        List<Movie> movieList = new ArrayList<>();

try{
    movieList =CursorUtils.movieCursorToList(movieCursor, context);
}catch (NullPointerException e){
    e.printStackTrace();
}

        return movieList;
    }

    public static boolean movieIsFavourite(int movieId, Context context) {
        String id = Integer.toString(movieId);
        Uri uri = MoviesContract.Favourites.CONTENT_URI.buildUpon().appendPath(id).build();

        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        if (cursor.moveToNext()) {
            cursor.close();

            return true;
        } else {
            cursor.close();

            return false;
        }
    }

    public static List<String> getGenreNames(int[] genreIds, Context context) {
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.Genres.CONTENT_URI,
                null,
                null,
                null,
                MoviesContract.Genres._ID);

        List<Genre> genreList = CursorUtils.genreCursorToList(cursor);
        cursor.close();

        List<String> genreNames = new ArrayList<>();
        for (int id : genreIds) {
            for (int i = 0; i < genreList.size(); i++) {
                if (genreList.get(i).getId() == id) {
                    genreNames.add(genreList.get(i).getName());
                }
            }
        }

        return genreNames;
    }

    static int[] getMovieGenres(int movieId, Context context) {
        String id = Integer.toString(movieId);

        Uri uri = MoviesContract.MovieGenres.CONTENT_URI.buildUpon().appendPath(id).build();
        Cursor movieGenresCursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                MoviesContract.MovieGenres._ID
        );

//movieGenresCursor.close();

        return CursorUtils.movieGenresCursorToArray(movieGenresCursor);
    }

}