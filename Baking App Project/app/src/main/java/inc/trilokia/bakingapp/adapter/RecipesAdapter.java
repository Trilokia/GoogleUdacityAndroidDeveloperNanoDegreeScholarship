package inc.trilokia.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.trilokia.bakingapp.R;
import inc.trilokia.bakingapp.callback.RecipeOnClickListener;
import inc.trilokia.bakingapp.model.Recipe;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private List<Recipe> mRecipes;
    private RecipeOnClickListener mCallback;

    public RecipesAdapter(RecipeOnClickListener callback) {
        mRecipes = new ArrayList<>();
        mCallback = callback;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes, parent, false);
        return new RecipeViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(holder.getAdapterPosition());
        Context context = holder.itemView.getContext();

        holder.recipeTitle.setText(recipe.getName());
        holder.recipeIngredient.setText(context.getString(R.string.recipe_ingredient, recipe.getIngredients().size()));
        holder.recipeStep.setText(context.getString(R.string.recipe_step, recipe.getSteps().size()));
        holder.recipeServing.setText(recipe.getServings() + " " + context.getString(R.string.recipe_serving));

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setDataAdapter(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public List<Recipe> getDataAdapter() {
        return mRecipes;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_recipes_title)
        TextView recipeTitle;

        @BindView(R.id.adapter_recipes_cardview)
        CardView recipeCardView;

        @BindView(R.id.adapter_recipes_ingredient)
        TextView recipeIngredient;

        @BindView(R.id.adapter_recipes_step)
        TextView recipeStep;

        @BindView(R.id.adapter_recipes_serving)
        TextView recipeServing;


        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recipeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetail(mRecipes.get(getAdapterPosition()));
                }
            });
        }
    }

    private void showDetail(Recipe recipe) {
        mCallback.onRecipeSelected(recipe);
    }
}