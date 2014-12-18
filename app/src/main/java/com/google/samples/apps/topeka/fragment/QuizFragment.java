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
package com.google.samples.apps.topeka.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.adapter.QuizPagerAdapter;
import com.google.samples.apps.topeka.adapter.ScoreAdapter;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

/**
 * Encapsulates Quiz solving and displays it to the user.
 */
public class QuizFragment extends Fragment {

    private Category mCategory;
    private ViewPager mViewPager;

    private static final String EXTRA_CURRENT_ITEM = "currentItem";

    public static QuizFragment newInstance(String categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("The category can not be null");
        }
        Bundle args = new Bundle();
        args.putString(Category.TAG, categoryId);
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String categoryId = getArguments().getString(Category.TAG);
        mCategory = TopekaDatabaseHelper.getCategoryWith(getActivity(), categoryId);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_CURRENT_ITEM, mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.quiz_pager);
        if (mCategory.isSolved()) {
            showSummary();
        } else {
            mViewPager.setBackgroundResource(mCategory.getTheme().getWindowBackgroundColor());
            mViewPager.setAdapter(new QuizPagerAdapter(getActivity(), mCategory));
            final int currentItem;
            if (null != savedInstanceState) {
                 currentItem = savedInstanceState.getInt(EXTRA_CURRENT_ITEM);
            } else {
                currentItem = mCategory.getFirstUnsolvedQuizPosition();
            }
            mViewPager.setCurrentItem(currentItem);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        TopekaDatabaseHelper.updateCategory(getActivity(), mCategory);
        super.onStop();
    }

    public boolean nextPage() {
        if (null == mViewPager) {
            return false;
        }
        int nextItem = mViewPager.getCurrentItem() + 1;
        if (nextItem < mViewPager.getAdapter().getCount()) {
            mViewPager.setCurrentItem(nextItem, true);
            return true;
        }
        mCategory.setSolved(true);
        return false;
    }

    public void showSummary() {
        final ListView scorecardView = (ListView) getView().findViewById(R.id.scorecard);
        scorecardView.setAdapter(new ScoreAdapter(mCategory));
        scorecardView.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
    }
}
