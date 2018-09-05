package com.example.bhati.bakers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakersWidget extends AppWidgetProvider {

    private static List<Recipe> mRecipes;
    private static RemoteViews sViews;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        sViews = new RemoteViews(context.getPackageName(), R.layout.bakers_widget);


        getData(context);


        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        sViews.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);

        Intent incrementIntent = new Intent(context,BakingWidgetService.class);
        incrementIntent.setAction(BakingWidgetService.ACTION_INCREMENT);
        PendingIntent incrementPendingIntent = PendingIntent.getService(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        sViews.setOnClickPendingIntent(R.id.widget_increment,incrementPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, sViews);
    }

    private static void getData(Context context) {
        deserialize(context);
        Log.v("Widget",mRecipes.size()+"");

    }

    private static void deserialize(Context context) {
        Type recipeListType = new TypeToken<List<Recipe>>() {
        }.getType();
        mRecipes = new Gson().fromJson(loadJSONFromAsset(context), recipeListType);
        StringBuilder builder = new StringBuilder(mRecipes.get(0).getName()+"\n\n");
//        for(Ingredient ingredient:mRecipes.get(0).getIngredients()){
//            builder.append(ingredient.getIngredient()+"\n");
//        }
        sViews.setTextViewText(R.id.appwidget_text, builder.toString());
    }

    public static String loadJSONFromAsset(Context context) {
        StringBuilder responseStrBuilder;
        try {
            InputStream is = context.getAssets().open("recipes.json");
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return responseStrBuilder.toString();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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

