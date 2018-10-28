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
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment implements StepAdapter.HandleClick{

    public static final String EXTRA_RECIPE = "recipeFromMain";
    private Recipe mRecipe;
    @BindView(R.id.rv_recipe) RecyclerView mRecyclerView;
    private StepAdapter mAdapter;
    @BindView(R.id.tv_ingredients) TextView mIngredientsTextView;
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
        ButterKnife.bind(this,rootView);

        setup();

        mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();

        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient :mIngredients) {
            builder.append("\u2022 ")
                    .append(ingredient.getIngredient())
                    .append(", ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure());

            if(ingredient.getQuantity()>1){
                builder.append("S");
            }

            builder.append("\n");
        }

        mIngredientsTextView.setText(builder.toString());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mAdapter = new StepAdapter(getActivity(),mSteps,this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

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
