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

import static com.example.bhati.bakers.StepFragment.EXTRA_POSITION;
import static com.example.bhati.bakers.StepFragment.EXTRA_RECIPE;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.HandleClick {


    private boolean mIsTwoPane;
    private static final String TAG = "RecipeActivity";
    private Recipe mRecipe;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            mPosition = savedInstanceState.getInt(EXTRA_POSITION, 0);
        }


        mIsTwoPane = findViewById(R.id.fragment_step_recipe) != null;
        if (mIsTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_step_recipe, StepFragment.newInstance(mRecipe, mPosition))
                    .commit();
            Log.v(TAG, "twoPane");
        } else {
            Log.v(TAG, "onePane");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putInt(EXTRA_POSITION, mPosition);
    }

    @Override
    public void onClick(Recipe recipe, int position) {
        if (mIsTwoPane) {

            mRecipe = recipe;
            mPosition = position;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_step_recipe, StepFragment.newInstance(recipe, position))
                    .commit();

        } else {
            Intent intent = new Intent(RecipeActivity.this, StepActivity.class);
            intent.putExtra(EXTRA_RECIPE, recipe);
            intent.putExtra(EXTRA_POSITION, position);
            startActivity(intent);
        }
    }

}
