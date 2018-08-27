package com.example.bhati.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.HandleClick{


    private boolean mIsTwoPane;
    private static final String TAG = "RecipeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mIsTwoPane = findViewById(R.id.fragment_step_recipe) != null;
        if(mIsTwoPane){
            Log.v(TAG,"twoPane");
        }else{
            Log.v(TAG,"onePane");
        }

    }


    @Override
    public void onClick(Recipe recipe,int position) {
        if(mIsTwoPane){
        }else {
            Intent intent = new Intent(RecipeActivity.this, StepActivity.class);
            intent.putExtra(StepFragment.EXTRA_RECIPE, recipe);
            startActivity(intent);
        }
    }

}
