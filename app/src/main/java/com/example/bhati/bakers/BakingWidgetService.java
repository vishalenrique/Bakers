package com.example.bhati.bakers;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class BakingWidgetService extends IntentService {

    public static final String ACTION_INCREMENT = "com.example.bhati.bakers.action.increment";
    public static final String ACTION_DECREMENT = "com.example.bhati.bakers.action.decrement";
    private static final String TAG = BakingWidgetService.class.getSimpleName();
    public static final String PREF_BAKING = "preferences";
    public static final String PREF_SIZE = "totalSize";
    public static final String PREF_CURRENT_INDEX = "currentIndex";
    private int mSize;
    private int mCurrentIndex;

    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INCREMENT.equals(action)) {
                increment();
            } else if (ACTION_DECREMENT.equals((action))) {
                decrement();
            }
        }
    }

    private void decrement() {
        SharedPreferences sharedPreferences = getValuesFromSharedPreferences();
        if (mCurrentIndex == 0) {
            Log.v(TAG, getString(R.string.first_item));
            showToast(getString(R.string.first_item));
            return;
        }
        mCurrentIndex--;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CURRENT_INDEX, mCurrentIndex);
        editor.apply();
        updateWidget();
        Log.v(TAG, "onHandleIntent with decrement action, called");
    }

    @NonNull
    private SharedPreferences getValuesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_BAKING, Context.MODE_PRIVATE);
        mSize = sharedPreferences.getInt(BakingWidgetService.PREF_SIZE, 0);
        mCurrentIndex = sharedPreferences.getInt(BakingWidgetService.PREF_CURRENT_INDEX, 0);
        return sharedPreferences;
    }

    private void increment() {
        SharedPreferences sharedPreferences = getValuesFromSharedPreferences();
        if (mCurrentIndex == (mSize - 1)) {
            Log.v(TAG, getString(R.string.last_item));
            showToast(getString(R.string.last_item));
            return;
        }
        mCurrentIndex++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CURRENT_INDEX, mCurrentIndex);
        editor.apply();
        updateWidget();
        Log.v(TAG, "onHandleIntent with increment action, called");
    }

    private void showToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BakingWidgetService.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakersWidget.class));
        BakersWidget.updateFromService(this, appWidgetManager, appWidgetIds);
    }
}
