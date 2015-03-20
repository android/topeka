/*
 * Copyright 2014 Google Inc.
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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.fragment.QuizFragment;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

import static com.google.samples.apps.topeka.adapter.CategoryAdapter.DRAWABLE;

public class QuizActivity extends Activity implements View.OnClickListener {

    private static final String IMAGE_CATEGORY = "image_category_";
    private static final String STATE_IS_PLAYING = "isPlaying";
    private static final int UNDEFINED = -1;
    private static final String FRAGMENT_TAG = "Quiz";
    private final Interpolator mBackgroundIconInterpolator = new DecelerateInterpolator();
    private String mCategoryId;
    private QuizFragment mQuizFragment;
    Toolbar mToolbar;
    private ImageView mStartQuiz;
    private boolean mIsPlaying;

    public static Intent getStartIntent(Context context, Category category) {
        Intent starter = new Intent(context, QuizActivity.class);
        starter.putExtra(Category.TAG, category.getId());
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCategoryId = getIntent().getStringExtra(Category.TAG);
        if (null != savedInstanceState) {
            mIsPlaying = savedInstanceState.getBoolean(STATE_IS_PLAYING);
        }
        populate(mCategoryId);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        if (mIsPlaying) {
            mQuizFragment = (QuizFragment) getFragmentManager().findFragmentByTag(
                    FRAGMENT_TAG);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_IS_PLAYING, mStartQuiz.getVisibility() == View.GONE);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_start_quiz:
                startQuizFromClickOn(v);
                break;
            case R.id.submitAnswer:
                submitAnswer();
                break;
            case UNDEFINED:
                if (v.getContentDescription().equals(getString(R.string.up))) {
                    onBackPressed();
                    break;
                }
            default:
                throw new UnsupportedOperationException(
                        "OnClick has not been implemented for " + getResources().getResourceName(
                                v.getId()));
        }
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.icon).animate().scaleX(0).scaleY(0).setStartDelay(0)
                .setInterpolator(mBackgroundIconInterpolator).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        QuizActivity.super.onBackPressed();
                        super.onAnimationEnd(animation);
                    }
                });
    }

    private void startQuizFromClickOn(final View view) {
        mQuizFragment = QuizFragment.newInstance(mCategoryId);
        getFragmentManager().beginTransaction()
                .replace(R.id.quiz_fragment_container, mQuizFragment, FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();

        view.animate().scaleX(0).scaleY(0).alpha(0)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                });
        // the toolbar should not have more elevation than the content while playing
        mToolbar.setElevation(0);
    }

    private void submitAnswer() {
        if (!mQuizFragment.nextPage()) {
            mQuizFragment.showSummary();
        }
    }

    private void populate(String categoryId) {
        if (null == categoryId) {
            //TODO: handle failing
            finish();
        }
        Category category = TopekaDatabaseHelper.getCategoryWith(this, categoryId);
        setTheme(category.getTheme().getStyleId());
        initLayout(category.getId());
        initToolbar(category);
    }

    private void initLayout(String categoryId) {
        setContentView(R.layout.activity_quiz);
        //TODO: 11/3/14 find a better way to do this, which doesn't include resource lookup.
        ImageView icon = (ImageView) findViewById(R.id.icon);
        int resId = getResources().getIdentifier(IMAGE_CATEGORY + categoryId, DRAWABLE,
                getApplicationContext().getPackageName());
        icon.setScaleX(0);
        icon.setScaleY(0);
        icon.setImageResource(resId);
        icon.animate().scaleX(1).scaleY(1).setInterpolator(mBackgroundIconInterpolator)
                .setStartDelay(300);
        mStartQuiz = (ImageView) findViewById(R.id.btn_start_quiz);
        mStartQuiz.setVisibility(mIsPlaying ? View.GONE : View.VISIBLE);
        mStartQuiz.setOnClickListener(this);
    }

    private void initToolbar(Category category) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_quiz);
        mToolbar.setTitle(category.getName());
        mToolbar.setNavigationOnClickListener(this);
        if (mIsPlaying) {
            // the toolbar should not have more elevation than the content while playing
            mToolbar.setElevation(0);
        }
    }
}
