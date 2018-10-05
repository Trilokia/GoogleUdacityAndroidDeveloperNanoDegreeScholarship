package inc.trilokia.popularmovies_2.ui.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.popularmovies_2.R;
import inc.trilokia.popularmovies_2.MyApplication;
import inc.trilokia.popularmovies_2.api.SearchReply;
import inc.trilokia.popularmovies_2.api.TheMovieDbApi;
import inc.trilokia.popularmovies_2.data.MoviesContract;
import inc.trilokia.popularmovies_2.models.Movie;
import inc.trilokia.popularmovies_2.ui.MovieAdapter;
import inc.trilokia.popularmovies_2.ui.MovieListOnScrollListener;
import inc.trilokia.popularmovies_2.utils.DatabaseUtils;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieListFragment extends Fragment implements
        MovieAdapter.MovieClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        SwipeRefreshLayout.OnRefreshListener {

    //Bound Views
    @BindView(R.id.list_fragment_imageview)
    View ivListIcon;
    @BindView(R.id.list_fragment_list_notification)
    TextView tvListNotification;
    @BindView(R.id.list_fragment_recycler_view)
    RecyclerView rvRecyclerView;

    //Bound SharedPreference Strings
    @BindString(R.string.pref_navigation_key)
    String NAVIGATION_KEY;
    @BindString(R.string.pref_navigation_discover_value)
    String NAVIGATION_DISCOVER_VALUE;
    @BindString(R.string.pref_navigation_search_value)
    String NAVIGATION_SEARCH_VALUE;
    @BindString(R.string.pref_navigation_favourites_value)
    String NAVIGATION_FAVOURITES_VALUE;
    @BindString(R.string.pref_discover_sort_key)
    String DISCOVER_SORT_KEY;
    @BindString(R.string.pref_discover_sort_popular_value)
    String DISCOVER_SORT_POPULAR_VALUE;
    @BindString(R.string.pref_discover_sort_rating_value)
    String DISCOVER_SORT_RATING_VALUE;
    @BindString(R.string.pref_favourites_sort_key)
    String FAVOURITES_SORT_KEY;
    @BindString(R.string.pref_favourites_sort_recent_value)
    String FAVOURITES_SORT_RECENT_VALUE;
    @BindString(R.string.pref_favourites_sort_title_value)
    String FAVOURITES_SORT_TITLE_VALUE;
    @BindString(R.string.pref_favourites_sort_release_value)
    String FAVOURITES_SORT_RELEASE_VALUE;
    @BindString(R.string.pref_language_key)
    String LANGUAGE_KEY;
    @BindString(R.string.pref_language_english_value)
    String LANGUAGE_ENGLISH_VALUE;

    //Bound Args Strings
    @BindString(R.string.args_movie_extra)
    String PASSED_MOVIE;

    //Bound Notification Strings
    @BindString(R.string.notification_list_base)
    String listNotificationBase;
    @BindString(R.string.notification_list_loading)
    String listNotificationLoading;
    @BindString(R.string.notification_list_search)
    String listNotificationSearch;
    @BindString(R.string.notification_list_favourites)
    String listNotificationFavourites;

    //Bound Strings
    @BindString(R.string.favourite_added_message)
    String favouriteAddedMessage;
    @BindString(R.string.favourite_removed_message)
    String favouriteRemovedMessage;
    @BindString(R.string.pref_discover_sort_label)
    String discoverSortLabel;
    @BindString(R.string.pref_discover_sort_popular_label)
    String discoverSortPopularLabel;
    @BindString(R.string.pref_discover_sort_rating_label)
    String discoverSortRatingLabel;
    @BindString(R.string.pref_favourites_sort_label)
    String favouritesSortLabel;

    //Bound Arrays
    @BindArray(R.array.pref_discover_sort_option_labels)
    String[] DISCOVER_SORT_OPTIONS;
    @BindArray(R.array.pref_favourites_sort_option_labels)
    String[] FAVOURITES_SORT_OPTIONS;

    //Args Strings
    private String mFragmentVersion;
    //TODO: implement with preference
    private static final String PORTRAIT_SPAN_COUNT_KEY = "portraitSpanCount";
    private static final String LANDSCAPE_SPAN_COUNT_KEY = "landscapeSpanCount";

    //Fragment Variables
    private OnMovieClickListener mCallback;
    private SharedPreferences mSharedPreferences;
    private List<Movie> mMovieList;
    private int mCurrentPage;
    private int mPortraitSpanCount;
    private int mLandscapeSpanCount;
    private SwipeRefreshLayout mSwipeRefresh;
    private int mSelectedMoviePosition = -1;
    private MovieAdapter mMovieAdapter;
    private int mTotalPages = 1;
    private MovieListOnScrollListener mScrollListener;

    private String mQueryParam;

    @Inject
    TheMovieDbApi api;

    public interface OnMovieClickListener {
        void onMovieSelected(Movie movie);
    }

    public MovieListFragment() {
    }

    public static MovieListFragment newInstance(int portraitSpanCount, int landscapeSpanCount) {
        Bundle args = new Bundle();
        args.putInt(PORTRAIT_SPAN_COUNT_KEY, portraitSpanCount);
        args.putInt(LANDSCAPE_SPAN_COUNT_KEY, landscapeSpanCount);

        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnMovieClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        ((MyApplication) getActivity().getApplication()).getNetworkComponent().inject(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mMovieList = new ArrayList<>();
        mCurrentPage = 0;

        mPortraitSpanCount = getArguments().getInt(PORTRAIT_SPAN_COUNT_KEY);
        mLandscapeSpanCount = getArguments().getInt(LANDSCAPE_SPAN_COUNT_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, rootView);

        mFragmentVersion = mSharedPreferences.getString(NAVIGATION_KEY, NAVIGATION_DISCOVER_VALUE);
        tvListNotification.setText(listNotificationLoading);
        ivListIcon.setVisibility(View.VISIBLE);

        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.list_fragment_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(this);

        initRecyclerView();
        loadMovieList(mQueryParam);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSelectedMoviePosition != -1) {
            mMovieAdapter.notifyItemChanged(mSelectedMoviePosition);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mFragmentVersion.equals(NAVIGATION_DISCOVER_VALUE)) {
            inflater.inflate(R.menu.menu_discover, menu);
            tvListNotification.setText(listNotificationLoading);
            ivListIcon.setVisibility(View.VISIBLE);
        } else if (mFragmentVersion.equals(NAVIGATION_SEARCH_VALUE)) {
            inflater.inflate(R.menu.menu_search, menu);
            tvListNotification.setText(listNotificationSearch);
            ivListIcon.setVisibility(View.VISIBLE);
            final MenuItem searchItem = menu.findItem(R.id.search_menu_action_search);
            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS |
                    InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchItem.collapseActionView();
                    searchView.clearFocus();

                    resetMovieList();
                    mQueryParam = query;
                    loadMovieList(mQueryParam);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        } else if (mFragmentVersion.equals(NAVIGATION_FAVOURITES_VALUE)) {
           inflater.inflate(R.menu.menu_favourites, menu);

            
            tvListNotification.setText(listNotificationFavourites);
            ivListIcon.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.discover_menu_action_sort) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(discoverSortLabel);
            builder.setItems(DISCOVER_SORT_OPTIONS, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        mSharedPreferences.edit().putString(DISCOVER_SORT_KEY,
                                DISCOVER_SORT_POPULAR_VALUE).apply();
                    } else if (which == 1) {
                        mSharedPreferences.edit().putString(DISCOVER_SORT_KEY,
                                DISCOVER_SORT_RATING_VALUE).apply();
                    }
                }
            });
            builder.show();

            return true;
        } else if (item.getItemId() == R.id.favourites_menu_action_sort){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(favouritesSortLabel);
            builder.setItems(FAVOURITES_SORT_OPTIONS, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        mSharedPreferences.edit().putString(FAVOURITES_SORT_KEY,
                                FAVOURITES_SORT_RECENT_VALUE).apply();
                    } else if (which == 1) {
                        mSharedPreferences.edit().putString(FAVOURITES_SORT_KEY,
                                FAVOURITES_SORT_TITLE_VALUE).apply();
                    } else if (which == 2) {
                        mSharedPreferences.edit().putString(FAVOURITES_SORT_KEY,
                                FAVOURITES_SORT_RELEASE_VALUE).apply();
                    }
                }
            });
            builder.show();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onListItemClick(View view, int itemIndex) {
        if (view.getId() == R.id.list_item_favourite_button) {
            String message;

            boolean removed = DatabaseUtils.toggleFavourite(mMovieList.get(itemIndex), getActivity());
            ImageButton button = (ImageButton) view;

            if (removed) {
                button.setImageResource(R.drawable.ic_favourite_empty);
                message = favouriteRemovedMessage;
            } else {
                button.setImageResource(R.drawable.ic_favourite_filled);
                message = favouriteAddedMessage;
            }

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else {
            mSelectedMoviePosition = itemIndex;
            try {
                mCallback.onMovieSelected(mMovieList.get(itemIndex));
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        try {
            getActivity().invalidateOptionsMenu();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        mFragmentVersion = mSharedPreferences.getString(NAVIGATION_KEY, NAVIGATION_DISCOVER_VALUE);

        resetMovieList();
        loadMovieList(mQueryParam);

        rvRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        resetMovieList();
        loadMovieList(mQueryParam);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager;
        if (getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 1);
        }

        rvRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(getActivity(), mMovieList, this);
        rvRecyclerView.setAdapter(mMovieAdapter);

        mScrollListener = new MovieListOnScrollListener(layoutManager) {
            @Override
            public void loadMore() {
                //TODO: Beware of modifying a recyclerView adapter outside of the UI thread...That might lead to an annoying java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position" if somewhere else the adapter is modified.
                //https://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView
                //https://stackoverflow.com/questions/39445330/cannot-call-notifyiteminserted-from-recyclerview-onscrolllistener
               rvRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Notify adapter with appropriate notify methods
                        mMovieAdapter.notifyItemInserted(mMovieList.size() - 1);
                    }
                });
               loadMovieList(mQueryParam);
            }
        };
        rvRecyclerView.addOnScrollListener(mScrollListener);
    }

    private void resetMovieList() {
        tvListNotification.setVisibility(View.VISIBLE);
        ivListIcon.setVisibility(View.VISIBLE);
        rvRecyclerView.setVisibility(View.GONE);

        mMovieList = new ArrayList<>();
        mMovieAdapter.setMovies(mMovieList);
        mCurrentPage = 0;
        mTotalPages = 1;
        //Toast.makeText(MyApplication.getInstance(), "test", Toast.LENGTH_SHORT).show();
    }

    private void loadMovieList(@Nullable String queryParam) {
        //Toast.makeText(MyApplication.getInstance(), "test1", Toast.LENGTH_SHORT).show();

        String languageParam = mSharedPreferences.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH_VALUE);

        if (mFragmentVersion.equals(NAVIGATION_DISCOVER_VALUE)) {
            String sortParam = mSharedPreferences.getString(DISCOVER_SORT_KEY,
                    DISCOVER_SORT_POPULAR_VALUE);

            mCurrentPage++;
            if (mCurrentPage > mTotalPages) return;
            int pageParam = mCurrentPage;

            String voteCountParam;

            if (sortParam.equals(DISCOVER_SORT_RATING_VALUE)) {
                voteCountParam = "500";
            } else {
                voteCountParam = "";
            }

            api.discover(sortParam,languageParam,voteCountParam,pageParam)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<SearchReply<Movie>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull SearchReply<Movie> movieSearchReply) {
                            if (movieSearchReply.getResults().size() != 0) {
                                tvListNotification.setVisibility(View.GONE);
                                ivListIcon.setVisibility(View.GONE);
                                rvRecyclerView.setVisibility(View.VISIBLE);

                                mMovieList.addAll(movieSearchReply.getResults());
                                mMovieAdapter.setMovies(mMovieList);
                                mMovieAdapter.notifyDataSetChanged();

                                mTotalPages = movieSearchReply.getTotalPages();

                                mScrollListener.setLoading(false);
                                mSwipeRefresh.setRefreshing(false);
                            } else {
                                tvListNotification.setText(listNotificationBase);
                                ivListIcon.setVisibility(View.VISIBLE);
                            }

                            mSwipeRefresh.setRefreshing(false);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                    });
        } else if (mFragmentVersion.equals(NAVIGATION_SEARCH_VALUE)) {
            mCurrentPage++;
            if (mCurrentPage > mTotalPages) return;
            int pageParam = mCurrentPage;

            api.search(languageParam, queryParam, pageParam)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<SearchReply<Movie>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull SearchReply<Movie> movieSearchReply) {
                            if (movieSearchReply.getResults().size() != 0) {
                                tvListNotification.setVisibility(View.GONE);
                                ivListIcon.setVisibility(View.GONE);
                                rvRecyclerView.setVisibility(View.VISIBLE);

                                mMovieList.addAll(movieSearchReply.getResults());
                                mMovieAdapter.setMovies(mMovieList);
                                mMovieAdapter.notifyDataSetChanged();

                                mTotalPages = movieSearchReply.getTotalPages();

                                mScrollListener.setLoading(false);
                            } else {
                                tvListNotification.setText(listNotificationBase);
                                ivListIcon.setVisibility(View.VISIBLE);
                            }

                            mSwipeRefresh.setRefreshing(false);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                    });
        } else if (mFragmentVersion.equals(NAVIGATION_FAVOURITES_VALUE)) {
            String sortParam = mSharedPreferences.getString(FAVOURITES_SORT_KEY,
                    MoviesContract.Favourites._ID);

            mMovieList = DatabaseUtils.getFavourites(MyApplication.getInstance(), sortParam);

            if (mMovieList.size() != 0) {
                tvListNotification.setVisibility(View.GONE);
                ivListIcon.setVisibility(View.GONE);
                rvRecyclerView.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovies(mMovieList);
                mMovieAdapter.notifyDataSetChanged();
            }

            mSwipeRefresh.setRefreshing(false);
        }
    }


}