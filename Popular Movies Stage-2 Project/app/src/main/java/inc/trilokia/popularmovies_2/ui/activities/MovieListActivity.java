package inc.trilokia.popularmovies_2.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.popularmovies_2.R;
import inc.trilokia.popularmovies_2.MyApplication;
import inc.trilokia.popularmovies_2.api.GenresReply;
import inc.trilokia.popularmovies_2.api.TheMovieDbApi;
import inc.trilokia.popularmovies_2.models.Genre;
import inc.trilokia.popularmovies_2.models.Movie;
import inc.trilokia.popularmovies_2.ui.fragments.MovieDetailsFragment;
import inc.trilokia.popularmovies_2.ui.fragments.MovieListFragment;
import inc.trilokia.popularmovies_2.utils.DatabaseUtils;
import inc.trilokia.popularmovies_2.utils.PrefUtils;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieListActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MovieListFragment.OnMovieClickListener {

    //Bound Views
    @BindView(R.id.list_activity_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.list_activity_drawer_layout)
    DrawerLayout dlDrawerLayout;
    @BindView(R.id.list_activity_nav_view)
    NavigationView nvNavigationView;
    @Nullable
    @BindView(R.id.list_activity_details_notification)
    TextView tvDetailsNotification;
    @Nullable
    @BindView(R.id.list_activity_details_scroll_view)
    ScrollView svDetailsScrollView;
    @Nullable
    @BindView(R.id.list_activity_details_container)
    LinearLayout llDetailsContainer;
@Nullable
    @BindView(R.id.nav_header_textView)
    TextView tvnavHeader;

    //Bound SharedPreference Strings
    @BindString(R.string.pref_first_run_key)
    String FIRST_RUN;
    @BindString(R.string.pref_navigation_key)
    String NAVIGATION_KEY;
    @BindString(R.string.pref_navigation_discover_value)
    String NAVIGATION_DISCOVER_VALUE;
    @BindString(R.string.pref_navigation_search_value)
    String NAVIGATION_SEARCH_VALUE;
    @BindString(R.string.pref_navigation_favourites_value)
    String NAVIGATION_FAVOURITES_VALUE;

    //Bound Action Strings
    @BindString(R.string.action_discover_label)
    String ACTION_DISCOVER_LABEL;
    @BindString(R.string.action_search_label)
    String ACTION_SEARCH_LABEL;
    @BindString(R.string.action_favourites_label)
    String ACTION_FAVOURITES_LABEL;

    //Bound Args Strings
    @BindString(R.string.args_selected_movie)
    String SELECTED_MOVIE;
    @BindString(R.string.args_movie_extra)
    String PASSED_MOVIE;
    @BindString(R.string.args_retained_fragment_tag)
    String RETAINED_FRAGMENT;

    //Activity Variables
    private ActionBarDrawerToggle mToggle;
    private SharedPreferences mSharedPreferences;
    private boolean mDualView;
    private Movie mSelectedMovie;
    private FragmentManager mFragmentManager;
    private int mPortraitSpanCount;
    private int mLandscapeSpanCount;

    @Inject
    TheMovieDbApi api;

    PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //https://developer.android.com/reference/android/os/StrictMode
Boolean DEVELOPER_MODE=false;
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        ((MyApplication) getApplication()).getNetworkComponent().inject(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setSupportActionBar(tbToolbar);
        prefUtils = new PrefUtils(this);
        boolean firstRun = mSharedPreferences.getBoolean(FIRST_RUN, true);
        //TODO: Should also run when app is updated
        if (firstRun) {
            loadGenres();
            mSharedPreferences.edit().putBoolean(FIRST_RUN, false).apply();
        }

        if (savedInstanceState != null) {
            mSelectedMovie = (Movie) savedInstanceState.getSerializable(SELECTED_MOVIE);
        }

        mFragmentManager = getSupportFragmentManager();

        initNavigationDrawer();
        initSpanCount();
        initFragment();
    }

    @Override
    public void onPostCreate(@android.support.annotation.Nullable Bundle savedInstanceState,
                             @android.support.annotation.Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSelectedMovie = null;
    }

    @Override
    public void onBackPressed() {
        if (dlDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            dlDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_MOVIE, mSelectedMovie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        } else if (mToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.navigation_discover:
                mSharedPreferences.edit().putString(NAVIGATION_KEY,
                        NAVIGATION_DISCOVER_VALUE).apply();
                dlDrawerLayout.closeDrawers();
                setTitle(ACTION_DISCOVER_LABEL);

                return true;
            case R.id.navigation_search:
                mSharedPreferences.edit().putString(NAVIGATION_KEY,
                        NAVIGATION_SEARCH_VALUE).apply();
                dlDrawerLayout.closeDrawers();
                setTitle(ACTION_SEARCH_LABEL);

                return true;
            case R.id.navigation_favourites:

                mSharedPreferences.edit().putString(NAVIGATION_KEY,
                        NAVIGATION_FAVOURITES_VALUE).apply();

                dlDrawerLayout.closeDrawers();

                setTitle(ACTION_FAVOURITES_LABEL);

                return true;
            default:
                return false;
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        mSelectedMovie = movie;
        if (mDualView) {
            tvDetailsNotification.setVisibility(View.GONE);
            svDetailsScrollView.setVisibility(View.VISIBLE);

            llDetailsContainer.removeAllViews();
            mFragmentManager.beginTransaction()
                    .replace(R.id.list_activity_details_container,
                            MovieDetailsFragment.newInstance(movie))
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(PASSED_MOVIE, movie);
            startActivity(intent);
        }
    }

    private void loadGenres() {
        api.getGenreList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GenresReply<Genre>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull GenresReply<Genre> genreGenresReply) {
                        DatabaseUtils.insertGenres(genreGenresReply.getGenres(),
                                MovieListActivity.this);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mSharedPreferences.edit().putBoolean(FIRST_RUN, false).apply();
                    }
                });
    }

    private void initNavigationDrawer() {
        mToggle = new ActionBarDrawerToggle(this, dlDrawerLayout, tbToolbar, 0, 0) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dlDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        nvNavigationView.setNavigationItemSelectedListener(this);
        //tvnavHeader.setText("Hello ");

        String navigationValue = mSharedPreferences.getString(NAVIGATION_KEY,
                NAVIGATION_DISCOVER_VALUE);
        String title;
        if (navigationValue.equals(NAVIGATION_SEARCH_VALUE)) {
            title = ACTION_SEARCH_LABEL;
            nvNavigationView.getMenu().getItem(1).setChecked(true);
        } else if (navigationValue.equals(NAVIGATION_FAVOURITES_VALUE)) {
            title = ACTION_FAVOURITES_LABEL;
            nvNavigationView.getMenu().getItem(2).setChecked(true);
        } else {
            title = ACTION_DISCOVER_LABEL;
            nvNavigationView.getMenu().getItem(0).setChecked(true);
        }
        setTitle(title);
    }

    private void initSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if (dpWidth < 600) {
            mPortraitSpanCount = 2;
            mLandscapeSpanCount = 1;
        } else if (dpWidth >= 600 && dpWidth < 720) {
            mPortraitSpanCount = 3;
            mLandscapeSpanCount = 2;
        } else if (dpWidth >= 720) {
            mPortraitSpanCount = 2;
            mLandscapeSpanCount = 2;
        }
    }

    private void initFragment() {
        if (llDetailsContainer == null) {
            mDualView = false;
        } else {
            mDualView = true;
        }
        
        MovieListFragment retainedFragment = (MovieListFragment)
                mFragmentManager.findFragmentByTag(RETAINED_FRAGMENT);

        if (retainedFragment == null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.list_activity_list_container,
                            MovieListFragment.newInstance(mPortraitSpanCount, mLandscapeSpanCount),
                            RETAINED_FRAGMENT)
                    .commit();
        }

        if (mDualView && mSelectedMovie != null) {
            tvDetailsNotification.setVisibility(View.GONE);
            svDetailsScrollView.setVisibility(View.VISIBLE);

            mFragmentManager.beginTransaction()
                    .replace(R.id.list_activity_details_container,
                            MovieDetailsFragment.newInstance(mSelectedMovie))
                    .commit();
        } else if (mDualView) {
            tvDetailsNotification.setVisibility(View.VISIBLE);
            svDetailsScrollView.setVisibility(View.GONE);
        }
    }

}