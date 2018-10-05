package inc.trilokia.popularmovies_2.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.popularmovies_2.R;
import inc.trilokia.popularmovies_2.MyApplication;
import inc.trilokia.popularmovies_2.api.ReviewsReply;
import inc.trilokia.popularmovies_2.api.TheMovieDbApi;
import inc.trilokia.popularmovies_2.api.TrailersReply;
import inc.trilokia.popularmovies_2.models.Movie;
import inc.trilokia.popularmovies_2.models.Review;
import inc.trilokia.popularmovies_2.models.Trailer;
import inc.trilokia.popularmovies_2.utils.DatabaseUtils;
import inc.trilokia.popularmovies_2.utils.StringFormattingUtils;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsFragment extends Fragment {
    ;

    @BindView(R.id.movie_details_title)
    TextView tvDetailsTitle;
    @BindView(R.id.details_fragment_details_genres)
    TextView tvDetailsGenres;
    @BindView(R.id.movie_details_release_date)
    TextView tvDetailsReleaseDate;
    @BindView(R.id.movie_details_vote_average)
    TextView tvDetailsVoteAverage;
    @BindView(R.id.details_fragment_overview)
    TextView tvOverview;
    @BindView(R.id.details_fragment_trailers_card)
    CardView cvTrailersCard;
    @BindView(R.id.details_fragment_trailers_layout)
    LinearLayout llTrailersLayout;
    @BindView(R.id.details_fragment_reviews_card)
    CardView cvReviewsCard;
    @BindView(R.id.details_fragment_reviews_layout)
    LinearLayout llReviewsLayout;

    //Bound Strings
    @BindString(R.string.genre_unknown)
    String genreUnknown;
    @BindString(R.string.details_fragment_details_vote_average_unavailable)
    String voteAverageUnavailable;

    //Args Strings
    private static final String MOVIE_KEY = "movie";

    //Fragment Variables
    private Movie mMovie;
    private LayoutInflater mInflater;
    private List<Trailer> mTrailerList;
    private List<Review> mReviewList;

    @Inject
    TheMovieDbApi api;

    public MovieDetailsFragment() {
    }

    public static MovieDetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(MOVIE_KEY, movie);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getNetworkComponent().inject(this);

        mMovie = (Movie) getArguments().getSerializable(MOVIE_KEY);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, rootView);

        mInflater = inflater;

        loadDataIntoViews();

        return rootView;
    }

    private void loadDataIntoViews() {

        tvDetailsTitle.setText(mMovie.getTitle());

        if (mMovie.getGenreIds().length != 0) {
            List<String> genreNames = DatabaseUtils.getGenreNames(mMovie.getGenreIds(), getActivity());
            tvDetailsGenres.setText(StringFormattingUtils.buildGenresString(genreNames));
        } else {
            tvDetailsGenres.setText(genreUnknown);
        }

        tvDetailsReleaseDate.setText(StringFormattingUtils.formatReleaseDate(mMovie.getReleaseDate()));

        float voteAverage = mMovie.getVoteAverage().floatValue();
        tvDetailsVoteAverage.setText(Float.toString(voteAverage)+"/10");
        if (voteAverage == 0) {
            tvDetailsVoteAverage.setText(voteAverageUnavailable);
        } else if (voteAverage > 0 && voteAverage < 2) {
            tvDetailsVoteAverage.setTextColor(getResources().getColor(R.color.voteNegative));
        } else if (voteAverage >= 2 && voteAverage < 4) {
            tvDetailsVoteAverage.setTextColor(getResources().getColor(R.color.voteBad));
        } else if (voteAverage >= 4 && voteAverage < 6) {
            tvDetailsVoteAverage.setTextColor(getResources().getColor(R.color.voteAverage));
        } else if (voteAverage >= 6 && voteAverage < 8) {
            tvDetailsVoteAverage.setTextColor(getResources().getColor(R.color.voteGood));
        } else if (voteAverage >= 8 && voteAverage <= 10) {
            tvDetailsVoteAverage.setTextColor(getResources().getColor(R.color.votePositive));
        }

        tvOverview.setText(mMovie.getOverview());

        getTrailers();

        getReviews();
    }

    private void getTrailers() {
        api.getTrailers(mMovie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TrailersReply<Trailer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull TrailersReply<Trailer> trailerTrailersReply) {
                        mTrailerList = trailerTrailersReply.getResults();

                        if (mTrailerList.size() >= 1) {
                            cvTrailersCard.setVisibility(View.VISIBLE);
                            for (Trailer trailer : mTrailerList) {
                                if (trailer.getType().equals("Trailer")) {
                                    llTrailersLayout.addView(createTrailerView(trailer.getName(),
                                            trailer.getKey()));
                                }
                            }
                        } else {
                            cvTrailersCard.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void getReviews() {
        api.getReviews(mMovie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ReviewsReply<Review>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ReviewsReply<Review> reviewReviewsReply) {
                        mReviewList = reviewReviewsReply.getResults();

                        if (mReviewList.size() >= 1) {
                            cvReviewsCard.setVisibility(View.VISIBLE);
                            for (Review review : mReviewList) {
                                llReviewsLayout.addView(createReviewView(review));
                            }
                        } else {
                            cvReviewsCard.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private TextView createTrailerView(String trailerName, String trailerKey) {
        TextView tvTrailer = (TextView) mInflater.inflate(R.layout.item_movie_trailer, null, false);

        tvTrailer.setText(trailerName);
        tvTrailer.setTag(trailerKey);

        tvTrailer.setOnClickListener(trailerClickListener);

        return tvTrailer;
    }

    private LinearLayout createReviewView(Review review) {
        LinearLayout llReview = (LinearLayout)
                mInflater.inflate(R.layout.item_movie_review, null, false);

        TextView tvAuthor = (TextView) llReview.findViewById(R.id.review_item_author);
        tvAuthor.setText(review.getAuthor());
        TextView tvContent = (TextView) llReview.findViewById(R.id.review_item_content);
        tvContent.setText(review.getContent());

        return llReview;
    }

    private View.OnClickListener trailerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    StringFormattingUtils.buildTrailerPath(v.getTag()));
            startActivity(intent);
        }
    };

}