package com.example.bhati.bakers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import android.support.test.espresso.IdlingResource;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import com.example.bhati.bakers.SimpleIdlingResource;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.HandleClick {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rv_main) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    private RecipeAdapter mAdapter;
    private List<Recipe> mRecipes;

    @Nullable
    private SimpleIdlingResource simpleIdlingResource;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        isConnected = isNetworkAvailable();

        getIdlingResource();


        Configuration configuration = getResources().getConfiguration();
        int smallestScreenWidthDp = configuration.smallestScreenWidthDp;


        mAdapter = new RecipeAdapter(this,mRecipes,this);
        mRecyclerView.setAdapter(mAdapter);

        if(smallestScreenWidthDp > 600){
            GridLayoutManager mLayoutManager = new GridLayoutManager(this,3);
            mRecyclerView.setLayoutManager(mLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,mLayoutManager.getOrientation());
            mRecyclerView.addItemDecoration(dividerItemDecoration);
        }else{
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,mLayoutManager.getOrientation());
            mRecyclerView.addItemDecoration(dividerItemDecoration);
        }

        if(isConnected) {
            deserialize();
        }else {
            Snackbar.make(mRecyclerView, getString(R.string.network_unavailable),Snackbar.LENGTH_SHORT).show();
        }

    }

    private void deserialize() {
        if(simpleIdlingResource !=null){
            simpleIdlingResource.setIdleState(false);
        }
        Call<List<Recipe>> recipeResponse = RecipeApi.getService().getRecipeList();
        recipeResponse.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mRecipes = response.body();
                mAdapter.setRecipes(mRecipes);
                saveToPreferences();

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this,  getString(R.string.response_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToPreferences() {
       Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(BakingWidgetService.PREF_BAKING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BakersWidget.PREF_DATA,gson.toJson(mRecipes));
        editor.apply();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this,RecipeActivity.class);
        intent.putExtra(RecipeFragment.EXTRA_RECIPE,recipe);
        startActivity(intent);
    }
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (simpleIdlingResource == null) {
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }
}
