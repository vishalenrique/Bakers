package com.example.bhati.bakers;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class StepActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "recipeFromRecipeActivity";
    private Recipe mRecipe;
    private List<Step> mSteps;
    private TextView mStepHeading;
    private TextView mStepDescription;
    private int mPosition = 0;
    private int mSize;


    PlayerView mPlayerView;
    SimpleExoPlayer mPlayer;
    private Step mStep;
    private String mVideoURL;
    private DefaultDataSourceFactory mDataSourceFactory;
    private ImageView mLeftArrowIV;
    private ImageView mRightArrowIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialSetup();

        mPlayerView = findViewById(R.id.exo_player_view);

        mSteps = mRecipe.getSteps();
        mSize = mSteps.size();
        mLeftArrowIV = findViewById(R.id.left_arrow);
        mRightArrowIV = findViewById(R.id.right_arrow);
        mStepHeading = findViewById(R.id.tv_step_heading);
        mStepDescription = findViewById(R.id.tv_step_description);

        mLeftArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == 0)
                    return;

                mPlayer.stop();
                --mPosition;
                displayCurrentStep();
                setupMediaSource();
            }
        });

        mRightArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == (mSize - 1))
                    return;

                mPlayer.stop();
                ++mPosition;
                displayCurrentStep();
                setupMediaSource();
            }
        });

        displayCurrentStep();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (!TextUtils.isEmpty(mVideoURL)) {
                hideOtherViews();

                getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                mPlayerView.setLayoutParams(params);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            showOtherViews();

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mPlayerView.setLayoutParams(params);
        }
    }

    private void hideOtherViews() {
        mLeftArrowIV.setVisibility(View.GONE);
        mRightArrowIV.setVisibility(View.GONE);
        mStepHeading.setVisibility(View.GONE);
        mStepDescription.setVisibility(View.GONE);
        getSupportActionBar().hide();
    }

    private void showOtherViews() {
        mLeftArrowIV.setVisibility(View.VISIBLE);
        mRightArrowIV.setVisibility(View.VISIBLE);
        mStepHeading.setVisibility(View.VISIBLE);
        mStepDescription.setVisibility(View.VISIBLE);
        getSupportActionBar().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupPlayer();
    }

    private void setupPlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        mPlayerView.setPlayer(mPlayer);
        mDataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Bakers"));
        setupMediaSource();
    }

    private void setupMediaSource() {
        if (TextUtils.isEmpty(mVideoURL)) {
            Snackbar.make(mStepHeading, "Video unavailable for this step", Snackbar.LENGTH_SHORT).show();
            mPlayerView.setVisibility(View.GONE);
            return;
        }

        mPlayerView.setVisibility(View.VISIBLE);
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(mDataSourceFactory)
                .createMediaSource(Uri.parse(mVideoURL));

        mPlayer.prepare(mediaSource);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.setPlayer(null);
        mPlayer.release();
        mPlayer = null;
    }

    private void initialSetup() {
        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        String recipeName = mRecipe.getName();
        getSupportActionBar().setTitle(recipeName);
    }

    private void displayCurrentStep() {
        mStep = mSteps.get(mPosition);
        mVideoURL = mStep.getVideoURL();
        mStepHeading.setText(mStep.getShortDescription());
        mStepDescription.setText(mStep.getDescription());
    }

}
