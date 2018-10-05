package inc.trilokia.popularmovies_2.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.popularmovies_2.R;
import inc.trilokia.popularmovies_2.models.Movie;
import inc.trilokia.popularmovies_2.ui.fragments.MovieDetailsFragment;
import inc.trilokia.popularmovies_2.utils.DatabaseUtils;
import inc.trilokia.popularmovies_2.utils.StringFormattingUtils;

public class MovieDetailsActivity extends AppCompatActivity implements
        View.OnClickListener {

    //Bound Views
    @BindView(R.id.details_activity_collapsing_toolbar)
    CollapsingToolbarLayout ctlCollapsingToolbar;
    @BindView(R.id.details_activity_backdrop)
    ImageView ivBackdrop;
    @BindView(R.id.details_activity_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.details_activity_favourite_button)
    FloatingActionButton fabFavouriteButton;

    //Bound Args Strings
    @BindString(R.string.args_movie_extra)
    String PASSED_MOVIE;

    //Bound Strings
    @BindString(R.string.favourite_added_message)
    String favouriteAddedMessage;
    @BindString(R.string.favourite_removed_message)
    String favouriteRemovedMessage;

    //Activity Variables
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        mMovie = (Movie) getIntent().getSerializableExtra(PASSED_MOVIE);

        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.details_activity_details_container,
                        MovieDetailsFragment.newInstance(mMovie))
                .commit();



    }

    @Override
    public void onClick(View v) {
        String message;

        boolean removed = DatabaseUtils.toggleFavourite(mMovie, this);

        if (removed) {
            fabFavouriteButton.setImageResource(R.drawable.ic_favourite_empty);
            message = favouriteRemovedMessage;
        } else {
            fabFavouriteButton.setImageResource(R.drawable.ic_favourite_filled);
            message = favouriteAddedMessage;
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        ctlCollapsingToolbar.setTitle(mMovie.getTitle());
        ctlCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this,
                android.R.color.transparent));

        Picasso.get()
                .load(StringFormattingUtils.buildImagePath(Movie.BACKDROP_LARGE,
                        mMovie.getBackdropPath()))
                .into(ivBackdrop);

        if (DatabaseUtils.movieIsFavourite(mMovie.getId(), this)) {
            fabFavouriteButton.setImageResource(R.drawable.ic_favourite_filled);
        } else {
            fabFavouriteButton.setImageResource(R.drawable.ic_favourite_empty);
        }
        fabFavouriteButton.setOnClickListener(this);
    }

}