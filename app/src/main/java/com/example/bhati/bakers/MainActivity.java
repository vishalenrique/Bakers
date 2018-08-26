package com.example.bhati.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.HandleClick {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mAdapter = new RecipeAdapter(this,mRecipes,this);
       //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        deserialize();

    }

    private void deserialize() {
        Type recipeListType = new TypeToken<List<Recipe>>() {
        }.getType();
        mRecipes = new Gson().fromJson(loadJSONFromAsset(), recipeListType);
        mAdapter.setRecipes(mRecipes);
    }

    public String loadJSONFromAsset() {
        StringBuilder responseStrBuilder;
        try {
            InputStream is = getAssets().open("recipes.json");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Recipe recipe) {
       // Snackbar.make(mRecyclerView,recipe.getName(),Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE,recipe);
        startActivity(intent);
    }
}
