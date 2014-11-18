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

import com.google.samples.apps.topeka.helper.ActivityHelper;
import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.fragment.QuizFragment;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Theme;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toolbar;

import static com.google.samples.apps.topeka.adapter.CategoryCursorAdapter.DRAWABLE;

public class QuizActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "QuizActivity";
    private static final String IMAGE_CATEGORY = "image_category_";
    private String mCategoryId;
    private QuizFragment mQuizFragment;

    public static Intent getStartIntent(Context context, Category category) {
        Intent starter = new Intent(context, QuizActivity.class);
        starter.putExtra(Category.TAG, category.getId());
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryId = getIntent().getStringExtra(Category.TAG);
        populate(mCategoryId);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_start_quiz:
                mQuizFragment = QuizFragment.newInstance(mCategoryId);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.quiz_fragment_container, mQuizFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                v.animate().alphaBy(-1f).setInterpolator(new AccelerateInterpolator(1f))
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                v.setVisibility(View.GONE);
                                super.onAnimationEnd(animation);
                            }
                        });
                break;

            case R.id.submitAnswer:
                if (!mQuizFragment.nextPage()) {
                    //TODO: 11/12/14 create summary page, then finish
                    finish();
                }
                break;
            default:
                throw new UnsupportedOperationException(
                        "OnClick has not been implemented for " + getResources().getResourceName(
                                v.getId()));
        }
    }

    private void populate(String categoryId) {
        if (null == categoryId) {
            //TODO: handle failing
            finish();
        }
        Category category = TopekaDatabaseHelper.getCategoryWith(this, categoryId);
        initLayout(category.getId());
        setTheme(category.getTheme());
        initToolbar(category);
    }

    private void initLayout(String categoryId) {
        setContentView(R.layout.activity_quiz);
        //TODO: 11/3/14 find a better way to do this, which doesn't include resource lookup.
        ImageView icon = (ImageView) findViewById(R.id.icon);
        int resId = getResources().getIdentifier(IMAGE_CATEGORY + categoryId, DRAWABLE,
                getApplicationContext().getPackageName());
        icon.setImageResource(resId);
        ImageView btnStartQuiz = (ImageView) findViewById(R.id.btn_start_quiz);
        btnStartQuiz.setOnClickListener(this);
    }

    private void setTheme(Theme theme) {
        if (null == theme) {
            Log.d(TAG, "No theme found. Falling back to default");
            theme = Theme.topeka;
        }
        setPrimaryColors(theme);
        setBackgroundColors(theme);
        Log.d(TAG, "Current theme is " + theme.name());
    }

    private void setPrimaryColors(Theme theme) {
        int colorPrimary = getColor(theme.getPrimaryColor());
        ActivityHelper.setStatusAndNavigationBarColor(this, colorPrimary);
        setActionBarColor(colorPrimary);
    }

    private void setBackgroundColors(Theme theme) {
        int colorBackground = getColor(theme.getWindowBackgroundColor());
        int colorPrimary = getColor(theme.getPrimaryColor());
        setBackgroundColor(R.id.quiz_container, colorBackground);
        setBackgroundColor(R.id.toolbar_quiz, colorPrimary);
    }

    private void setBackgroundColor(@IdRes int viewResId, int colorBackground) {
        findViewById(viewResId).setBackgroundColor(colorBackground);
    }

    private void initToolbar(Category category) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_quiz);
        toolbar.setTitle(category.getName());
    }

    private int getColor(int colorId) {
        Resources resources = getResources();
        return resources.getColor(colorId);
    }

    private void setActionBarColor(int colorPrimary) {
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setBackgroundDrawable(new ColorDrawable(colorPrimary));
        }
        getWindow().setNavigationBarColor(colorPrimary);
    }
}
