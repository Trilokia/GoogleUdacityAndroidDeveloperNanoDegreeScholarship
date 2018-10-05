package inc.trilokia.popularmovies_2.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import inc.trilokia.popularmovies_2.R;
import inc.trilokia.popularmovies_2.models.Movie;
import inc.trilokia.popularmovies_2.ui.views.PosterView;
import inc.trilokia.popularmovies_2.utils.DatabaseUtils;
import inc.trilokia.popularmovies_2.utils.StringFormattingUtils;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //Class Variables
    private Context mContext;
    private List<Movie> mMovies;
    private MovieClickListener mMovieClickListener;

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
    }

    public interface MovieClickListener {
        void onListItemClick(View view, int itemIndex);
    }

    public MovieAdapter(Context context, List<Movie> movies, MovieClickListener movieClickListener) {
        mContext = context;
        mMovies = movies;
        mMovieClickListener = movieClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PosterView pvPoster;
        TextView tvTitle;
        LinearLayout llBar;
        TextView tvGenres;
        ImageButton ibFavouriteButton;

        public ViewHolder(View v) {
            super(v);
            pvPoster = (PosterView) v.findViewById(R.id.list_item_poster);
            tvTitle = (TextView) v.findViewById(R.id.list_item_title);
            llBar = (LinearLayout) v.findViewById(R.id.list_item_bar);
            tvGenres = (TextView) v.findViewById(R.id.list_item_genres);
            ibFavouriteButton = (ImageButton) v.findViewById(R.id.list_item_favourite_button);
            ibFavouriteButton.setOnClickListener(this);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemIndex = getAdapterPosition();
            try {
                mMovieClickListener.onListItemClick(v, itemIndex);
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_movie_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mMovies.get(position).getPosterPath() != null) {
            holder.pvPoster.setVisibility(View.VISIBLE);
            holder.tvTitle.setVisibility(View.GONE);

            String posterPath = StringFormattingUtils.buildImagePath(Movie.POSTER_MEDIUM,
                    mMovies.get(position).getPosterPath());


            Picasso.get()
                    .load(posterPath).into(holder.pvPoster);
        } else {
            holder.pvPoster.setVisibility(View.INVISIBLE);
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(mMovies.get(position).getTitle());
        }

        if (mMovies.get(position).getGenreIds().length != 0) {
            int[] genreIds = mMovies.get(position).getGenreIds();
            List<String> genreNames = DatabaseUtils.getGenreNames(genreIds, mContext);
            holder.tvGenres.setText(StringFormattingUtils.buildGenresString(genreNames));
        } else {
            holder.tvGenres.setText("Unknown");
        }

        if (DatabaseUtils.movieIsFavourite(mMovies.get(position).getId(), mContext)) {
            holder.ibFavouriteButton.setImageResource(R.drawable.ic_favourite_filled);
        } else {
            holder.ibFavouriteButton.setImageResource(R.drawable.ic_favourite_empty);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        } else {
            return 0;
        }
    }

}