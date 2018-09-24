package inc.trilokia.popularmovies_1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import inc.trilokia.popularmovies_1.models.Movie;

import java.util.List;

//TODO:(For Stage 2) Bind Views with ButterKnife
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;
    private MovieClickListener mMovieClickListener;

    /**
     * The interface which receives onClick messages.
     */
    public interface MovieClickListener {
        void onListItemClick(int itemIndex);
    }

    /**
     * MovieAdapter constructor.
     *
     * @param context The context this adapter will be used in
     * @param movies List of Movie objects this adapter will display
     * @param movieClickListener The MovieClickListener this adapter will use.
     */
    public MovieAdapter(Context context, List<Movie> movies, MovieClickListener movieClickListener) {
        mContext = context;
        mMovies = movies;
        mMovieClickListener = movieClickListener;
    }

    /**
     * Caches the views in the Adapter
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePoster;

        /**
         * Gets reference to inflated Layout's TextView and ImageView, applies an onClickListener
         * to the inflated Layout.
         *
         * @param v The view created in onCreateViewHolder().
         */
        public ViewHolder(View v) {
            super(v);
            moviePoster = (ImageView) v.findViewById(R.id.movie_preview_poster);
            v.setOnClickListener(this);
        }

        /**
         * Called upon user click.
         *
         * @param v The View that was clicked.
         */
        @Override
        public void onClick(View v) {
            int itemIndex = getAdapterPosition();
            mMovieClickListener.onListItemClick(itemIndex);
        }
    }

    /**
     * Inflates a Layout to be added to a ViewHolder.
     *
     * @param parent The ViewGroup in which the ViewHolders are stored
     * @param viewType Unused remnant of superclass
     * @return A ViewHolder which contains a list of movie_previews.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_preview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    /**
     * Applies a Movie title and poster at each view in the ViewHolder.
     *
     * @param holder The ViewHolder to be updated
     * @param position The position of the Movie to be applied to this ViewHolder.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.get().load(mMovies.get(position).getPosterPath(Movie.POSTER_MEDIUM))
                .into(holder.moviePoster);
    }

    /**
     * Returns the number of items available to the Adapter.
     *
     * @return The number of Movies in mMovies.
     */
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}
