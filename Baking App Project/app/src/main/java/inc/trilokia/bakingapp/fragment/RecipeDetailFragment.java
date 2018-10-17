package inc.trilokia.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.bakingapp.R;
import inc.trilokia.bakingapp.adapter.RecipeStepsAdapter;
import inc.trilokia.bakingapp.app.BakingApp;
import inc.trilokia.bakingapp.callback.RecipeStepOnClickListener;
import inc.trilokia.bakingapp.event.RecipeStepEvent;
import inc.trilokia.bakingapp.model.Ingredient;
import inc.trilokia.bakingapp.model.Step;

import static inc.trilokia.bakingapp.util.Constant.Data.EXTRA_INGREDIENTS;
import static inc.trilokia.bakingapp.util.Constant.Data.EXTRA_STEPS;


public class RecipeDetailFragment extends Fragment implements RecipeStepOnClickListener {
    @BindView(R.id.detail_ingredients)
    TextView mDetailIngredients;

    @BindView(R.id.detail_steps)
    RecyclerView mDetailSteps;

    private List<Ingredient> mRecipeIngredients;
    private List<Step> mRecipeSteps;

    public RecipeDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frecipe_detail, container, false);

        initView(rootView);

        Bundle bundle = getArguments();
        String strIngredients = bundle.getString(EXTRA_INGREDIENTS);
        mRecipeIngredients = Arrays.asList(BakingApp.getInstance().getGson().fromJson(strIngredients, Ingredient[].class));

        String strSteps = bundle.getString(EXTRA_STEPS);
        mRecipeSteps = Arrays.asList(BakingApp.getInstance().getGson().fromJson(strSteps, Step[].class));

        String strIngredient = "";
        for (Ingredient ingredient : mRecipeIngredients) {
            DecimalFormat format = new DecimalFormat("#.##");

            strIngredient += "- " + format.format(ingredient.getQuantity())
                    + " " + ingredient.getMeasure() + " of " + ingredient.getIngredient() + ".";
            strIngredient += "\n";
        }

        mDetailIngredients.setText(strIngredient);

        LinearLayoutManager recipeStepLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mDetailSteps.setLayoutManager(recipeStepLayoutManager);

        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(this);
        mDetailSteps.setAdapter(recipeStepsAdapter);
        recipeStepsAdapter.setDataAdapter(mRecipeSteps);

        ViewCompat.setNestedScrollingEnabled(mDetailSteps, false);

        return rootView;
    }

    private void initView(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onStepSelected(int selectedPosition) {
        EventBus eventBus = BakingApp.getInstance().getEventBus();
        RecipeStepEvent event = new RecipeStepEvent();
        event.setSelectedPosition(selectedPosition);
        eventBus.post(event);
    }
}