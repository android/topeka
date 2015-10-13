/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.topeka.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.fragment.QuizFragment;
import com.google.samples.apps.topeka.helper.ApiLevelHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

import static com.google.samples.apps.topeka.adapter.CategoryAdapter.DRAWABLE;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String IMAGE_CATEGORY = "image_category_";
    private static final String STATE_IS_PLAYING = "isPlaying";
    private static final int UNDEFINED = -1;
    private static final String FRAGMENT_TAG = "Quiz";

    private Interpolator mInterpolator;
    private String mCategoryId;
    private QuizFragment mQuizFragment;
    private Toolbar mToolbar;
    private FloatingActionButton mQuizFab;
    private boolean mSavedStateIsPlaying;
    private ImageView mIcon;
    private Animator mCircularReveal;
    private CountingIdlingResource mCountingIdlingResource;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.fab_quiz:
                    startQuizFromClickOn(v);
                    break;
                case R.id.submitAnswer:
                    submitAnswer();
                    break;
                case R.id.quiz_done:
                    ActivityCompat.finishAfterTransition(QuizActivity.this);
                    break;
                case UNDEFINED:
                    final CharSequence contentDescription = v.getContentDescription();
                    if (contentDescription != null && contentDescription
                            .equals(getString(R.string.up))) {
                        onBackPressed();
                        break;
                    }
                default:
                    throw new UnsupportedOperationException(
                            "OnClick has not been implemented for " + getResources().
                                    getResourceName(v.getId()));
            }
        }
    };

    public static Intent getStartIntent(Context context, Category category) {
        Intent starter = new Intent(context, QuizActivity.class);
        starter.putExtra(Category.TAG, category.getId());
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCountingIdlingResource = new CountingIdlingResource("Quiz");
        mCategoryId = getIntent().getStringExtra(Category.TAG);
        mInterpolator = new FastOutSlowInInterpolator();
        if (null != savedInstanceState) {
            mSavedStateIsPlaying = savedInstanceState.getBoolean(STATE_IS_PLAYING);
        }
        super.onCreate(savedInstanceState);
        populate(mCategoryId);
    }

    @Override
    protected void onResume() {
        if (mSavedStateIsPlaying) {
            mQuizFragment = (QuizFragment) getSupportFragmentManager().findFragmentByTag(
                    FRAGMENT_TAG);
            findViewById(R.id.quiz_fragment_container).setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_IS_PLAYING, mQuizFab.getVisibility() == View.GONE);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mIcon == null || mQuizFab == null) {
            // Skip the animation if icon or fab are not initialized.
            super.onBackPressed();
            return;
        }

        // Scale the icon and fab to 0 size before calling onBackPressed if it exists.
        ViewCompat.animate(mIcon)
                .scaleX(.7f)
                .scaleY(.7f)
                .alpha(0f)
                .setInterpolator(mInterpolator)
                .start();

        ViewCompat.animate(mQuizFab)
                .scaleX(0f)
                .scaleY(0f)
                .setInterpolator(mInterpolator)
                .setStartDelay(100)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onAnimationEnd(View view) {
                        if (isFinishing() ||
                                (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1)
                                        && isDestroyed())) {
                            return;
                        }
                        QuizActivity.super.onBackPressed();
                    }
                })
                .start();
    }

    private void startQuizFromClickOn(final View clickedView) {
        initQuizFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.quiz_fragment_container, mQuizFragment, FRAGMENT_TAG).commit();
        final View fragmentContainer = findViewById(R.id.quiz_fragment_container);
        revealFragmentContainer(clickedView, fragmentContainer);
        // the toolbar should not have more elevation than the content while playing
        setToolbarElevation(false);
    }

    private void revealFragmentContainer(final View clickedView, final View fragmentContainer) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            revealFragmentContainerLollipop(clickedView, fragmentContainer);
        } else {
            fragmentContainer.setVisibility(View.VISIBLE);
            clickedView.setVisibility(View.GONE);
            mIcon.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealFragmentContainerLollipop(final View clickedView,
                                                 final View fragmentContainer) {
        prepareCircularReveal(clickedView, fragmentContainer);
        ViewCompat.animate(clickedView)
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(mInterpolator)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        fragmentContainer.setVisibility(View.VISIBLE);
                        mCircularReveal.start();
                        clickedView.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void prepareCircularReveal(View startView, View targetView) {
        int centerX = (startView.getLeft() + startView.getRight()) / 2;
        int centerY = (startView.getTop() + startView.getBottom()) / 2;
        float finalRadius = (float) Math.hypot((double) centerX, (double) centerY);
        mCircularReveal = ViewAnimationUtils.createCircularReveal(
                targetView, centerX, centerY, 0, finalRadius);

        mCircularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIcon.setVisibility(View.GONE);
                mCircularReveal.removeListener(this);
            }
        });
    }

    public void setToolbarElevation(boolean shouldElevate) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            mToolbar.setElevation(shouldElevate ?
                    getResources().getDimension(R.dimen.elevation_header) : 0);
        }
    }

    private void initQuizFragment() {
        mQuizFragment = QuizFragment.newInstance(mCategoryId,
                new QuizFragment.SolvedStateListener() {
                    @Override
                    public void onCategorySolved() {
                        setToolbarElevation(true);
                        displayDoneFab();
                    }

                    private void displayDoneFab() {
                        /* We're re-using the already existing fab and give it some
                         * new values. This has to run delayed due to the queued animation
                         * to hide the fab initially.
                         */
                        if (null != mCircularReveal && mCircularReveal.isRunning()) {
                            mCircularReveal.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    showQuizFabWithDoneIcon();
                                    mCircularReveal.removeListener(this);
                                }
                            });
                        } else {
                            showQuizFabWithDoneIcon();
                        }
                    }

                    private void showQuizFabWithDoneIcon() {
                        mQuizFab.setImageResource(R.drawable.ic_tick);
                        mQuizFab.setId(R.id.quiz_done);
                        mQuizFab.setVisibility(View.VISIBLE);
                        mQuizFab.setScaleX(0f);
                        mQuizFab.setScaleY(0f);
                        ViewCompat.animate(mQuizFab)
                                .scaleX(1)
                                .scaleY(1)
                                .setInterpolator(mInterpolator)
                                .setListener(null)
                                .start();
                    }
                });
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            // the toolbar should not have more elevation than the content while playing
            setToolbarElevation(false);
        }
    }

    /**
     * Proceeds the quiz to it's next state.
     */
    public void proceed() {
        submitAnswer();
    }

    /**
     * Solely exists for testing purposes and making sure Espresso does not get confused.
     */
    public void lockIdlingResource() {
        mCountingIdlingResource.increment();
    }

    private void submitAnswer() {
        mCountingIdlingResource.decrement();
        if (!mQuizFragment.showNextPage()) {
            mQuizFragment.showSummary();
            return;
        }
        setToolbarElevation(false);
    }

    private void populate(String categoryId) {
        if (null == categoryId) {
            Log.w(TAG, "Didn't find a category. Finishing");
            finish();
        }
        Category category = TopekaDatabaseHelper.getCategoryWith(this, categoryId);
        setTheme(category.getTheme().getStyleId());
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this,
                    category.getTheme().getPrimaryDarkColor()));
        }
        initLayout(category.getId());
        initToolbar(category);
    }

    private void initLayout(String categoryId) {
        setContentView(R.layout.activity_quiz);
        //noinspection PrivateResource
        mIcon = (ImageView) findViewById(R.id.icon);
        int resId = getResources().getIdentifier(IMAGE_CATEGORY + categoryId, DRAWABLE,
                getApplicationContext().getPackageName());
        mIcon.setImageResource(resId);
        mIcon.setImageResource(resId);
        ViewCompat.animate(mIcon)
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(mInterpolator)
                .setStartDelay(300)
                .start();
        mQuizFab = (FloatingActionButton) findViewById(R.id.fab_quiz);
        mQuizFab.setImageResource(R.drawable.ic_play);
        if (mSavedStateIsPlaying) {
            mQuizFab.hide();
        } else {
            mQuizFab.show();
        }
        mQuizFab.setOnClickListener(mOnClickListener);
    }

    private void initToolbar(Category category) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_quiz);
        mToolbar.setBackgroundColor(
                ContextCompat.getColor(this, category.getTheme().getPrimaryColor()));
        mToolbar.setTitle(category.getName());
        mToolbar.setNavigationOnClickListener(mOnClickListener);
        if (mSavedStateIsPlaying) {
            // the toolbar should not have more elevation than the content while playing
            setToolbarElevation(false);
        }
    }

    @VisibleForTesting
    public CountingIdlingResource getCountingIdlingResource() {
        return mCountingIdlingResource;
    }
}
