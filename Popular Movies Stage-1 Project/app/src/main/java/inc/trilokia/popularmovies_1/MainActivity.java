package inc.trilokia.popularmovies_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import inc.trilokia.popularmovies_1.models.Movie;
import inc.trilokia.popularmovies_1.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

//TODO: (For Stage 2) Add Javadocs
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener {

    //Bound Views
    @BindView(R.id.movie_list)
    RecyclerView rvMovieList;

    //Bound Strings
    @BindString(R.string.sort_menu_title)
    String sortMenuTitle;
    @BindString(R.string.most_popular)
    String mostPopular;
    @BindString(R.string.highest_rating)
    String highestRating;
    @BindString(R.string.intent_movie_extra)
    String PASSED_MOVIE;

    //Activity variables
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList;
    private String languageParam;

    //JSON Object key titles
    private static final String RESULT_ARRAY = "results",
            TITLE = "title",
            POSTER_PATH = "poster_path",
            OVERVIEW = "overview",
            RELEASE_DATE = "release_date",
            VOTE_AVERAGE = "vote_average";

    /**
     * calls loadMovies() and sets a LayoutManager and Adapter
     * to the RecyclerView used within the associated layout.
     *
     * @param savedInstanceState Bundle object containing activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //TODO: Allow user to modify language in later versions (Yet to implement)
        languageParam = NetworkUtils.LANGUAGE_EN;

        loadMovies(NetworkUtils.SORT_BY_POPULARITY);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvMovieList.setLayoutManager(layoutManager);
        rvMovieList.setAdapter(mMovieAdapter);
    }

    /**
     * Creates a new AsyncTask to load movies based on sort and language parameters.
     *
     * @param sortParam The desired sort parameter
     *  languageParam The desired language parameter.
     */
    private void loadMovies(String sortParam) {
        new FetchMovieData().execute(NetworkUtils.buildUrl(sortParam));
    }

    /**
     * Makes a call to themoviedb.org's API, and creates a list of Movie objects from the
     * resulting JSON Object.
     */
    public class FetchMovieData extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(URL... params) {
            List<Movie> movieList = new ArrayList<>();

            URL query = params[0];
            String jsonResponse = null;

            try {
                jsonResponse = NetworkUtils.getResponseFromServer(query);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jsonResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray results = jsonObject.getJSONArray(RESULT_ARRAY);

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie = results.getJSONObject(i);
                        String title = movie.getString(TITLE);
                        String posterPath = movie.getString(POSTER_PATH);
                        String overview = movie.getString(OVERVIEW);
                        String releaseDate = movie.getString(RELEASE_DATE);
                        Number voteAverage = (Number) movie.get(VOTE_AVERAGE);

                        Movie temp = new Movie(title, posterPath, overview, releaseDate,
                                voteAverage);

                        movieList.add(temp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            mMovieList = movieList;

            mMovieAdapter = new MovieAdapter(getApplicationContext(), movieList, MainActivity.this);
            rvMovieList.setAdapter(mMovieAdapter);
        }

    }

    /**
     * Inflates a menu layout resource into the Menu provided in the callback.
     *
     * @param menu The desired layout resource to inflate
     * @return True.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Amends the movie list based on the sort option clicked by the user.
     *
     * @param item The menu item clicked by the user
     * @return True if handled.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_sort_dialog) {
            CharSequence sortOptions[] = new CharSequence[]{mostPopular, highestRating};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(sortMenuTitle);
            builder.setItems(sortOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        Toast.makeText(MainActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();

                        loadMovies(NetworkUtils.SORT_BY_POPULARITY);
                    } else if (which == 1) {
                        Toast.makeText(MainActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();

                        loadMovies(NetworkUtils.SORT_BY_RATING);
                    }
                }
            });

            builder.show();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Launches the MovieDetails activity, passing the clicked movie along with the intent.
     *
     * @param itemIndex The index of the movie clicked by the user.
     */
    @Override
    public void onListItemClick(int itemIndex) {
        Movie clickedMovie = mMovieList.get(itemIndex);

        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra(PASSED_MOVIE, clickedMovie);
        startActivity(intent);
    }

}
