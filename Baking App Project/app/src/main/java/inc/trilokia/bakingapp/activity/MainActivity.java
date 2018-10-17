package inc.trilokia.bakingapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.bakingapp.R;
import inc.trilokia.bakingapp.adapter.RecipesAdapter;
import inc.trilokia.bakingapp.app.BakingApp;
import inc.trilokia.bakingapp.app.SimpleIdlingResource;
import inc.trilokia.bakingapp.callback.RecipeOnClickListener;
import inc.trilokia.bakingapp.controller.RecipeController;
import inc.trilokia.bakingapp.event.RecipeEvent;
import inc.trilokia.bakingapp.model.Recipe;

import static inc.trilokia.bakingapp.util.Constant.Data.EXTRA_RECIPE;
import static inc.trilokia.bakingapp.util.Constant.Data.LIST_DATA;
import static inc.trilokia.bakingapp.util.Constant.Data.LIST_NEED_LOADING;
import static inc.trilokia.bakingapp.util.Constant.Data.LIST_STATE;
import static inc.trilokia.bakingapp.util.Constant.Data.MAIN_COLUMN_WIDTH_DEFAULT;
import static inc.trilokia.bakingapp.util.Constant.Function.nextActivity;

public class MainActivity extends AppCompatActivity implements RecipeOnClickListener {
    @BindView(R.id.main_recipes_refresh)
    SwipeRefreshLayout mMainRecipesRefresh;

    @BindView(R.id.main_recipes)
    RecyclerView mRvRecipes;

    @BindView(R.id.main_progress_layout)
    RelativeLayout mProgressLayout;

    @BindView(R.id.main_error_layout)
    RelativeLayout mErrorLayout;

    private RecipesAdapter mRecipeAdapter;
    private EventBus eventBus;
    private boolean mNeedReload = true;


    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventBus = BakingApp.getInstance().getEventBus();

        initView();

        if (savedInstanceState != null) {
            mRvRecipes.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE));
            mRecipeAdapter.setDataAdapter(Arrays.asList(BakingApp.getInstance().getGson().fromJson(savedInstanceState.getString(LIST_DATA), Recipe[].class)));
            mNeedReload = savedInstanceState.getBoolean(LIST_NEED_LOADING);
        }

        mMainRecipesRefresh.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
        mMainRecipesRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMainRecipesRefresh.setRefreshing(false);
                getRecipes();
            }
        });

        if (mNeedReload) getRecipes();
    }

    private void getRecipes() {
        mRvRecipes.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);

        RecipeController controller = new RecipeController();
        controller.getRecipes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    private void initView() {
        ButterKnife.bind(this);

        int columns = getColumnCountByWidth();

        RecyclerView.LayoutManager recipeLayoutManager = new GridLayoutManager(this, columns, LinearLayoutManager.VERTICAL, false);
        mRvRecipes.setLayoutManager(recipeLayoutManager);

        mRecipeAdapter = new RecipesAdapter(this);
        mRvRecipes.setAdapter(mRecipeAdapter);
    }

    private int getColumnCountByWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth / MAIN_COLUMN_WIDTH_DEFAULT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE, mRvRecipes.getLayoutManager().onSaveInstanceState());
        outState.putString(LIST_DATA, BakingApp.getInstance().getGson().toJson(mRecipeAdapter.getDataAdapter()));
        outState.putBoolean(LIST_NEED_LOADING, mNeedReload);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecipes(RecipeEvent event) {
        mProgressLayout.setVisibility(View.GONE);
        if (event.isSuccess()) {
            mRecipeAdapter.setDataAdapter(event.getRecipes());
            mErrorLayout.setVisibility(View.GONE);
            mRvRecipes.setVisibility(View.VISIBLE);
            mNeedReload = false;
        } else {
            mNeedReload = true;
            mRvRecipes.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RECIPE, BakingApp.getInstance().getGson().toJson(recipe));

        nextActivity(this, RecipeActivity.class, bundle, false);
    }


}