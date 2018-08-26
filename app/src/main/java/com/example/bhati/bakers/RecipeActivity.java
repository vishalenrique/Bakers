package com.example.bhati.bakers;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class RecipeActivity extends AppCompatActivity implements StepAdapter.HandleClick{

    public static final String EXTRA_RECIPE = "recipeFromMain";
    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private StepAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        temp(toolbar);
        mRecyclerView = findViewById(R.id.rv_recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mAdapter = new StepAdapter(this,mRecipe.getSteps(),this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void temp(Toolbar toolbar) {
        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        String recipeName = mRecipe.getName();

        getSupportActionBar().setTitle(recipeName);
    }

    @Override
    public void onClick(Step step) {
        Snackbar.make(mRecyclerView,step.getShortDescription(),Snackbar.LENGTH_SHORT).show();
    }
}
