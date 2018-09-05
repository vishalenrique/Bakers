package com.example.bhati.bakers;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class BakingWidgetService extends IntentService {

    public static final String ACTION_INCREMENT = "com.example.bhati.bakers.action.increment";
    private static final String TAG = BakingWidgetService.class.getSimpleName();

    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_INCREMENT.equals(action)){
                Toast.makeText(this, "onHandleIntent called", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
