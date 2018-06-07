package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    /* NOTE:
     * go_xxx refers to disabling textView & labels
     * vis_xxx refers to enabling textView & labels
     * xxx_action refers to Onclick View
     * */

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.image_iv)
    ImageView mIngredientsIv;

    @BindView(R.id.ove_lab)
    TextView mOverviewLabel;

    @BindView(R.id.desc_lab)
    TextView mDescriptionLabel;

    @BindView(R.id.ing_lab)
    TextView mIngredientLabel;

    @BindView(R.id.also_known_tv)
    TextView mAlsoKnownTv;

    @BindView(R.id.alsoKnownAs_label)
    TextView mAlsoKnownLabel;

    @BindView(R.id.origin_tv)
    TextView mOriginTv;

    @BindView(R.id.placeOfOrigin_label)
    TextView mOriginLabel;

    @BindView(R.id.description_tv)
    TextView mDescriptionTv;

    @BindView(R.id.ingredients_tv)
    TextView mIngredientTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // bind the view using butterknife
        ButterKnife.bind(this);

        init();

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mIngredientsIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Yeah :)
                    }

                    @Override
                    public void onError() {
                        mIngredientsIv.setImageResource(R.mipmap.error);
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        //* Also known as
        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {

            String str = null;
            for (int i = 1; i <= sandwich.getAlsoKnownAs().size(); i++) {
                str = TextUtils.join(", ", sandwich.getAlsoKnownAs());
            }
            mAlsoKnownTv.setText(str);
        } else {
            mAlsoKnownTv.setText(getString(R.string.detail_error_message));
        }

        //* Origin
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            mOriginTv.setText(getString(R.string.detail_error_message));
        } else {
            mOriginTv.setText(sandwich.getPlaceOfOrigin());
        }

        //* Description
        if (sandwich.getDescription().isEmpty()) {
            mDescriptionTv.setText(getString(R.string.detail_error_message));
        } else {
            mDescriptionTv.setText(sandwich.getDescription());

        }

        //*Ingredients
        if (sandwich.getIngredients() != null && sandwich.getIngredients().size() > 0) {
            String str = null;
            for (int i = 1; i <= sandwich.getIngredients().size(); i++) {
                str = TextUtils.join(", \n", sandwich.getIngredients());
            }
            mIngredientTv.setText(str);
        }


    }

    public void overAction(View view) {
        descriptionGone();
        ingredientGo();
        overviewVisible();
    }

    public void descAction(View view) {
        overviewGone();
        ingredientGo();
        descriptionVisible();
    }

    public void ingAction(View view) {
        overviewGone();
        descriptionGone();
        ingredientVisible();
    }

    private void overviewVisible() {
        init();
        mAlsoKnownLabel.setVisibility(View.VISIBLE);
        mAlsoKnownTv.setVisibility(View.VISIBLE);
        mOriginLabel.setVisibility(View.VISIBLE);
        mOriginTv.setVisibility(View.VISIBLE);

    }

    private void overviewGone() {
        mAlsoKnownLabel.setVisibility(View.GONE);
        mAlsoKnownTv.setVisibility(View.GONE);
        mOriginLabel.setVisibility(View.GONE);
        mOriginTv.setVisibility(View.GONE);
    }

    private void descriptionVisible() {
        mOverviewLabel.setTextColor(Color.GRAY);
        mDescriptionLabel.setTextColor(Color.WHITE);
        mIngredientLabel.setTextColor(Color.GRAY);
        mDescriptionTv.setVisibility(View.VISIBLE);
    }

    private void descriptionGone() {
        mDescriptionTv.setVisibility(View.GONE);
    }

    private void ingredientVisible() {
        mOverviewLabel.setTextColor(Color.GRAY);
        mDescriptionLabel.setTextColor(Color.GRAY);
        mIngredientLabel.setTextColor(Color.WHITE);
        mIngredientTv.setVisibility(View.VISIBLE);
    }

    private void ingredientGo() {
        mIngredientTv.setVisibility(View.GONE);
    }

    private void init() {
        mOverviewLabel.setTextColor(Color.WHITE);
        mDescriptionLabel.setTextColor(Color.GRAY);
        mIngredientLabel.setTextColor(Color.GRAY);

    }

}
