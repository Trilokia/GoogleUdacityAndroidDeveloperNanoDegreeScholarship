package inc.trilokia.popularmovies_1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import inc.trilokia.popularmovies_1.models.Movie;

import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {

    @BindView(R.id.movie_details_overview)
    TextView tvOverview;
    @BindView(R.id.movie_details_title)
    TextView tvTitle;
    @BindView(R.id.movie_details_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.movie_details_vote_average)
    TextView tvVoteAverage;
    @BindView(R.id.movie_details_poster)
    ImageView ivPoster;

    @BindString(R.string.intent_movie_extra)
    String PASSED_MOVIE;

    /**
     * Retrieves Movie object stored in Intent, calls loadMovieData().
     *
     * @param savedInstanceState Bundle object containing activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);

        Movie movie = (Movie) getIntent().getSerializableExtra(PASSED_MOVIE);
        loadMovieData(movie);
    }

    /**
     * Loads data from a Movie object into the activity's Views.
     *
     * @param movie The Movie object to load data from.
     */
    private void loadMovieData(Movie movie) {
        setTitle(movie.getTitle());

        // Loads images
        Picasso.get().load(movie.getPosterPath(Movie.POSTER_LARGE)).into(ivPoster);

        tvOverview.setText(movie.getOverview());
        tvTitle.append("\"" + movie.getTitle() + "\"");
        tvReleaseDate.append(movie.getReleaseDate());
        tvVoteAverage.append(movie.getVoteAverage() + "/10");


    }


}
