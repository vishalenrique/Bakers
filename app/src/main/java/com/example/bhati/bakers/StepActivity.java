package com.example.bhati.bakers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StepActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "recipeFromRecipeActivity";
    private Recipe mRecipe;
    private List<Step> mSteps;
    private TextView mStepHeading;
    private TextView mStepDescription;
    private int mPosition = 0;
    private int mSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialSetup();

        mSteps = mRecipe.getSteps();
        mSize = mSteps.size();
        ImageView leftArrowIV = findViewById(R.id.left_arrow);
        ImageView rightArrowIV = findViewById(R.id.right_arrow);
        mStepHeading = findViewById(R.id.tv_step_heading);
        mStepDescription = findViewById(R.id.tv_step_description);

        leftArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPosition == 0)
                    return;

                --mPosition;
                displayCurrentStep();
            }
        });

        rightArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPosition == (mSize-1))
                    return;

                ++mPosition;
                displayCurrentStep();
            }
        });

        displayCurrentStep();

    }

    private void initialSetup() {
        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        String recipeName = mRecipe.getName();
        getSupportActionBar().setTitle(recipeName);
    }

    private void displayCurrentStep() {
        Step step = mSteps.get(mPosition);
        mStepHeading.setText(step.getShortDescription());
        mStepDescription.setText(step.getDescription());
    }

}
