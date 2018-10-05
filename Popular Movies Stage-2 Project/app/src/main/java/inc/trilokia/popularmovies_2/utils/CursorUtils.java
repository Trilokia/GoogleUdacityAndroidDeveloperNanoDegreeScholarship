package inc.trilokia.popularmovies_2.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import inc.trilokia.popularmovies_2.data.MoviesContract;
import inc.trilokia.popularmovies_2.models.Genre;
import inc.trilokia.popularmovies_2.models.Movie;

public final class CursorUtils {

    static List<Movie> movieCursorToList(Cursor movieCursor, Context context) {
        List<Movie> movieList = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            String posterPath = movieCursor.getString(movieCursor.getColumnIndex(
                    MoviesContract.Favourites.COLUMN_POSTER_PATH));
            String overview = movieCursor.getString(movieCursor.getColumnIndex(
                    MoviesContract.Favourites.COLUMN_OVERVIEW));
            String releaseDate = movieCursor.getString(movieCursor.getColumnIndex(
                    MoviesContract.Favourites.COLUMN_RELEASE_DATE));
            int[] genreIds = DatabaseUtils.getMovieGenres(movieCursor.getInt(
                    movieCursor.getColumnIndex(MoviesContract.Favourites.COLUMN_MOVIE_ID)), context);
            int id = movieCursor.getInt(movieCursor.getColumnIndex(
                    MoviesContract.Favourites.COLUMN_MOVIE_ID));
            String title = movieCursor.getString(movieCursor.getColumnIndex(
                    MoviesContract.Favourites.COLUMN_TITLE));
            String backdropPath = movieCursor.getString(movieCursor.getColumnIndex(
                    MoviesContract.Favourites.COLUMN_BACKDROP_PATH));
            Number voteAverage = movieCursor.getInt(movieCursor.getColumnIndex(
                    MoviesContract.Favourites.COLUMN_VOTE_AVERAGE));

            Movie temp = new Movie(posterPath, overview, releaseDate, genreIds, id,
                    title, backdropPath, voteAverage);
            movieList.add(temp);
        }

        movieCursor.close();

        return movieList;
    }

    static List<Genre> genreCursorToList(Cursor genreCursor) {
        List<Genre> genreList = new ArrayList<>();

        while (genreCursor.moveToNext()) {
            int id = genreCursor.getInt(genreCursor.getColumnIndex(
                    MoviesContract.Genres.COLUMN_GENRE_ID));
            String name = genreCursor.getString(genreCursor.getColumnIndex(
                    MoviesContract.Genres.COLUMN_GENRE_NAME));

            Genre temp = new Genre(id, name);
            genreList.add(temp);
        }

        genreCursor.close();

        return genreList;
    }

    static int[] movieGenresCursorToArray(Cursor movieGenresCursor) {
        int[] genreIds = new int[movieGenresCursor.getCount()];

        for (int i = 0; i < movieGenresCursor.getCount(); i++) {
            movieGenresCursor.moveToPosition(i);
            int id = movieGenresCursor.getInt(movieGenresCursor.getColumnIndex(
                    MoviesContract.MovieGenres.COLUMN_GENRE_ID));
            genreIds[i] = id;
        }

        movieGenresCursor.close();
        return genreIds;
    }

}