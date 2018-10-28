package com.example.bhati.bakers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepFragment extends Fragment {

    public static final String EXTRA_RECIPE = "recipeFromRecipeActivity";
    public static final String EXTRA_POSITION = "extraPosition";
    private Recipe mRecipe;
    private List<Step> mSteps;
    @BindView(R.id.tv_step_heading) TextView mStepHeading;
    @BindView(R.id.tv_step_description) TextView mStepDescription;
    private int mPosition = 0;
    private int mSize;


    @BindView(R.id.exo_player_view) PlayerView mPlayerView;
    SimpleExoPlayer mPlayer;
    private Step mStep;
    private String mVideoURL;
    private DefaultDataSourceFactory mDataSourceFactory;
    @BindView(R.id.left_arrow) ImageView mLeftArrowIV;
    @BindView(R.id.right_arrow) ImageView mRightArrowIV;
    private View mRootView;
    private boolean mIsConnected;
    private Snackbar mSnackbar;
    private static final String TAG = "StepFragment";


    public StepFragment() {
        // Required empty public constructor
    }


    public static StepFragment newInstance(Recipe recipe, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, recipe);
        bundle.putInt(EXTRA_POSITION, position);
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);
        return stepFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this,mRootView);

        mIsConnected = isNetworkAvailable();
        if(!mIsConnected)
        Toast.makeText(getActivity(), getActivity().getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();

        if(savedInstanceState==null){
            Intent intent = getActivity().getIntent();
            mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
            mPosition = intent.getIntExtra(EXTRA_POSITION, 0);
            if (mRecipe == null) {
                Bundle arguments = getArguments();
                mRecipe = arguments.getParcelable(EXTRA_RECIPE);
                mPosition = arguments.getInt(EXTRA_POSITION);
            }

            if (mRecipe != null) {
                initialSetup(null, mPosition);
            }
        }

        return mRootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();
    }

    public void initialSetup(Recipe recipe, int position) {
        if (mRecipe == null) {
            mRecipe = recipe;
            mPosition = position;
        }

        mSteps = mRecipe.getSteps();
        mSize = mSteps.size();

        mLeftArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == 0) {
                    Snackbar.make(mStepHeading,getActivity().getString(R.string.first_item), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(mSnackbar.isShown())
                mSnackbar.dismiss();
                --mPosition;
                displayCurrentStep();
                if(mIsConnected) {
                    mPlayer.stop();
                    setupMediaSource();
                }
            }
        });

        mRightArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == (mSize - 1)) {
                    Snackbar.make(mStepHeading,getActivity().getString(R.string.last_item), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(mSnackbar.isShown())
                mSnackbar.dismiss();
                ++mPosition;
                displayCurrentStep();
                if(mIsConnected) {
                    mPlayer.stop();
                    setupMediaSource();
                }
            }
        });

        displayCurrentStep();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mRecipe != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                if (!TextUtils.isEmpty(mVideoURL)) {
                    hideOtherViews();

                    getActivity().getWindow()
                            .getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mPlayerView.setLayoutParams(params);
                }
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

                showOtherViews();

                getActivity().getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mPlayerView.setLayoutParams(params);
            }
        }
    }

    private void hideOtherViews() {
        mLeftArrowIV.setVisibility(View.GONE);
        mRightArrowIV.setVisibility(View.GONE);
        mStepHeading.setVisibility(View.GONE);
        mStepDescription.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private void showOtherViews() {
        mLeftArrowIV.setVisibility(View.VISIBLE);
        mRightArrowIV.setVisibility(View.VISIBLE);
        mStepHeading.setVisibility(View.VISIBLE);
        mStepDescription.setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRecipe != null) {
            mSnackbar = Snackbar.make(mStepHeading, "Video unavailable for this step", Snackbar.LENGTH_INDEFINITE);
            if(mIsConnected)
            setupPlayer();
        }
    }

    private void setupPlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
        mPlayerView.setPlayer(mPlayer);
        mDataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "Bakers"));
        setupMediaSource();
    }

    private void setupMediaSource() {
        if (TextUtils.isEmpty(mVideoURL)) {
            mSnackbar.show();
            mPlayer.stop(true);
            return;
        }

        mPlayerView.setVisibility(View.VISIBLE);
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(mDataSourceFactory)
                .createMediaSource(Uri.parse(mVideoURL));

        mPlayer.prepare(mediaSource);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecipe != null && mIsConnected) {
            mPlayerView.setPlayer(null);
            mPlayer.release();
            mPlayer = null;
        }
        if(mSnackbar.isShown())
            mSnackbar.dismiss();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mRecipe != null) {
            String recipeName = mRecipe.getName();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }
    }

    private void displayCurrentStep() {
        mStep = mSteps.get(mPosition);
        mVideoURL = mStep.getVideoURL();
        mStepHeading.setText(mStep.getShortDescription());
        mStepDescription.setText(mStep.getDescription());
    }

}
