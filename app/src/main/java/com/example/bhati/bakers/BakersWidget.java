package com.example.bhati.bakers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class BakersWidget extends AppWidgetProvider {

    public static final String PREF_DATA = "pref_data";
    private static List<Recipe> mRecipes;
    private static RemoteViews sViews;
    private static int sSize;
    private static int sCurrentIndex;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        sViews = new RemoteViews(context.getPackageName(), R.layout.bakers_widget);


        deserialize(context);


        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(RecipeFragment.EXTRA_RECIPE,mRecipes.get(sCurrentIndex));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        sViews.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        Intent incrementIntent = new Intent(context, BakingWidgetService.class);
        incrementIntent.setAction(BakingWidgetService.ACTION_INCREMENT);
        PendingIntent incrementPendingIntent = PendingIntent.getService(
                context,
                0,
                incrementIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        sViews.setOnClickPendingIntent(R.id.widget_increment, incrementPendingIntent);

        Intent decrementIntent = new Intent(context, BakingWidgetService.class);
        decrementIntent.setAction(BakingWidgetService.ACTION_DECREMENT);
        PendingIntent decrementPendingIntent = PendingIntent.getService(
                context,
                0,
                decrementIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        sViews.setOnClickPendingIntent(R.id.widget_decrement, decrementPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, sViews);
    }

    private static void deserialize(final Context context) {
        Type recipeListType = new TypeToken<List<Recipe>>() {
        }.getType();
        SharedPreferences sharedPreferences = context.getSharedPreferences(BakingWidgetService.PREF_BAKING, Context.MODE_PRIVATE);
        mRecipes = new Gson().fromJson(sharedPreferences.getString(PREF_DATA,""),recipeListType);

        sCurrentIndex = sharedPreferences.getInt(BakingWidgetService.PREF_CURRENT_INDEX, 0);

        sSize = mRecipes.size();
        sharedPreferences.edit().putInt(BakingWidgetService.PREF_SIZE, sSize).apply();


        showContent(sCurrentIndex);

    }

    private static void showContent(int index) {
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : mRecipes.get(index).getIngredients()) {
            builder.append("\u2022 ")
                    .append(ingredient.getIngredient())
                    .append(", ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure());

            if(ingredient.getQuantity()>1){
                builder.append("S");
            }

            builder.append("\n");
        }
        sViews.setTextViewText(R.id.appwidget_recipe_name, mRecipes.get(index).getName());
        sViews.setTextViewText(R.id.appwidget_text, builder.toString());
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateFromService(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

