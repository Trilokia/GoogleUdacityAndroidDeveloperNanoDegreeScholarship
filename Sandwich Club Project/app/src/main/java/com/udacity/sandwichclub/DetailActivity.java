package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {


    /* NOTE:
    * go_xxx refers to disabling textView & labels
    * vis_xxx refers to enabling textView & labels
    * xxx_action refers to Onclick View
    * */

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ImageView mingredientsIv;
    private TextView mOverviewLabel;
    private TextView mDescriptionLabel;
    private TextView mIngredientLabel;
    private TextView mAlsoKnownTv;
    private TextView mAlsoKnownLabel;
    private TextView mOriginTv;
    private TextView mOriginLabel;
    private TextView mDescriptionTv;
    private TextView mIngredientTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mingredientsIv = findViewById(R.id.image_iv);
        mOverviewLabel = findViewById(R.id.ove_lab);
        mDescriptionLabel = findViewById(R.id.desc_lab);
        mIngredientLabel = findViewById(R.id.ing_lab);
        mAlsoKnownTv = findViewById(R.id.also_known_tv);
        mAlsoKnownLabel = findViewById(R.id.alsoKnownAs_label);
        mOriginTv = findViewById(R.id.origin_tv);
        mOriginLabel = findViewById(R.id.placeOfOrigin_label);
        mDescriptionTv = findViewById(R.id.description_tv);
        mIngredientTv = findViewById(R.id.ingredients_tv);


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
                .into(mingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
    //* Also known as
        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getAlsoKnownAs().get(0));
            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append(sandwich.getAlsoKnownAs().get(i));
            }
            mAlsoKnownTv.setText(stringBuilder.toString());
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
            StringBuilder stringBuilder = new StringBuilder();
            int index = 1;

            stringBuilder.append(index);
            stringBuilder.append(". ");
            stringBuilder.append(sandwich.getIngredients().get(0));

            for (int i = 1; i < sandwich.getIngredients().size(); i++) {
                stringBuilder.append("\n");
                stringBuilder.append(++index);
                stringBuilder.append(". ");
                stringBuilder.append(sandwich.getIngredients().get(i));
            }
            mIngredientTv.setText(stringBuilder.toString());
        }


    }

    public void over_action(View view) {
        go_Description();
        go_Ing();
        vis_Overview();
    }

    public void desc_action(View view) {
        go_Overview();
        go_Ing();
        vis_Description();
    }

    public void ing_action(View view) {
        go_Overview();
        go_Description();
        vis_Ing();
    }

    private void vis_Overview() {
        init();
        mAlsoKnownLabel.setVisibility(View.VISIBLE);
        mAlsoKnownTv.setVisibility(View.VISIBLE);
        mOriginLabel.setVisibility(View.VISIBLE);
        mOriginTv.setVisibility(View.VISIBLE);
    }

    private void go_Overview() {
        mAlsoKnownLabel.setVisibility(View.GONE);
        mAlsoKnownTv.setVisibility(View.GONE);
        mOriginLabel.setVisibility(View.GONE);
        mOriginTv.setVisibility(View.GONE);
    }

    private void vis_Description() {
        mOverviewLabel.setTextColor(Color.GRAY);
        mDescriptionLabel.setTextColor(Color.WHITE);
        mIngredientLabel.setTextColor(Color.GRAY);
        mDescriptionTv.setVisibility(View.VISIBLE);
    }

    private void go_Description() {
        mDescriptionTv.setVisibility(View.GONE);
    }

    private void vis_Ing() {
        mOverviewLabel.setTextColor(Color.GRAY);
        mDescriptionLabel.setTextColor(Color.GRAY);
        mIngredientLabel.setTextColor(Color.WHITE);
        mIngredientTv.setVisibility(View.VISIBLE);
    }

    private void go_Ing() {
        mIngredientTv.setVisibility(View.GONE);
    }

    private void init() {
        mOverviewLabel.setTextColor(Color.WHITE);
        mDescriptionLabel.setTextColor(Color.GRAY);
        mIngredientLabel.setTextColor(Color.GRAY);

    }

}
