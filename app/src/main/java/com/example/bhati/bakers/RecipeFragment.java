package com.example.bhati.bakers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecipeFragment extends Fragment implements StepAdapter.HandleClick{

    public static final String EXTRA_RECIPE = "recipeFromMain";
    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private StepAdapter mAdapter;
    private RecyclerView mIngredientsRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;

    public interface HandleClick{
        void onClick(Recipe recipe,int position);
    }
    public HandleClick mHandleClick;

    public RecipeFragment() {
        // Required empty public constructor
    }

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        setup();

        mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();


        mRecyclerView = rootView.findViewById(R.id.rv_recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mAdapter = new StepAdapter(getActivity(),mSteps,this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mIngredientsRecyclerView = rootView.findViewById(R.id.rv_recipe_ingredients);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mIngredientAdapter = new IngredientAdapter(getActivity(), mIngredients);
        mIngredientsRecyclerView.setLayoutManager(layoutManager1);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

        return rootView;
    }

    private void setup() {
        mRecipe = getActivity().getIntent().getParcelableExtra(EXTRA_RECIPE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String recipeName = mRecipe.getName();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipeName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mHandleClick = (HandleClick) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(int position) {
        mHandleClick.onClick(mRecipe,position);
    }

}
