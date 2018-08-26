package com.example.bhati.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

public class RecipeActivity extends AppCompatActivity implements StepAdapter.HandleClick{

    public static final String EXTRA_RECIPE = "recipeFromMain";
    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private StepAdapter mAdapter;
    private RecyclerView mIngredientsRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();

        mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();


        mRecyclerView = findViewById(R.id.rv_recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mAdapter = new StepAdapter(this,mSteps,this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mIngredientsRecyclerView = findViewById(R.id.rv_recipe_ingredients);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mIngredientAdapter = new IngredientAdapter(this, mIngredients);
        mIngredientsRecyclerView.setLayoutManager(layoutManager1);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);
    }

    private void setup() {
        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        String recipeName = mRecipe.getName();
        getSupportActionBar().setTitle(recipeName);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(RecipeActivity.this,StepActivity.class);
        intent.putExtra(StepActivity.EXTRA_RECIPE,mRecipe);
        startActivity(intent);
    }
}
