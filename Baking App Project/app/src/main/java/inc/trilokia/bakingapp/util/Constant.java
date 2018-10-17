package inc.trilokia.bakingapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class Constant {
    public static class Data {
        public static final String LIST_STATE = "list_state";
        public static final String LIST_DATA = "list_data";
        public static final String LIST_NEED_LOADING = "list_need_loading";
        public static final String EXTRA_RECIPE = "recipe";
        public static final String EXTRA_INGREDIENTS = "ingredients";
        public static final String EXTRA_STEPS = "steps";
        public static final String EXTRA_STEP = "step";
        public static final String EXTRA_STEP_POSITION = "step_position";
        public static final String PLAYER_POSITION = "player_position";
        public static final String EXTRA_STEP_NUMBER = "step_number";
        public static final String EXTRA_STEP_FIRST = "step_first";
        public static final String EXTRA_STEP_LAST = "step_last";
        public static final String EXTRA_IS_RECIPE_MENU = "is_recipe_menu";
        public static final String WIDGET_ID = "widget_id";
        public static final String BAKING_APP_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        public static final float MAIN_COLUMN_WIDTH_DEFAULT = 360;
    }

    public static class Function {

        public static void nextActivity(Context context, Class destClass, boolean isFinishActivity) {
            nextActivity(context, destClass, new Bundle(), isFinishActivity);
        }

        public static void nextActivity(Context context, Class destClass, Bundle bundle, boolean isFinishActivity) {
            Intent intent = new Intent(context, destClass);
            intent.putExtras(bundle);
            context.startActivity(intent);
            if (isFinishActivity) ((Activity) context).finish();
        }

    }
}