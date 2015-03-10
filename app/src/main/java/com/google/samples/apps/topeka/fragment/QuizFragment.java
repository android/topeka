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
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ListView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.adapter.QuizAdapter;
import com.google.samples.apps.topeka.adapter.ScoreAdapter;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;
import com.google.samples.apps.topeka.widget.quiz.AbsQuizView;

/**
 * Encapsulates Quiz solving and displays it to the user.
 */
public class QuizFragment extends Fragment {

    private static final String KEY_USER_INPUT = "USER_INPUT";

    private Category mCategory;
    private AdapterViewFlipper mQuizView;
    private ScoreAdapter mScoreAdapter;
    private QuizAdapter mQuizAdapter;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mQuizView = (AdapterViewFlipper) view.findViewById(R.id.quiz_pager);
        // TODO: 1/27/15 finalize animations
        mQuizView.setInAnimation(getActivity(), android.R.animator.fade_in);
        mQuizView.setOutAnimation(getActivity(), android.R.animator.fade_out);
        if (mCategory.isSolved()) {
            showSummary();
        } else {
            mQuizView.setAdapter(getQuizAdapter());
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        View currentView = mQuizView.getCurrentView();
        if (currentView instanceof ViewGroup) {
            ViewGroup currentViewGroup = (ViewGroup) currentView;
            View currentChild = currentViewGroup.getChildAt(0);
            if (currentChild instanceof AbsQuizView) {
                outState.putBundle(KEY_USER_INPUT, ((AbsQuizView) currentChild).getUserInput());
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        restoreQuizState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    private void restoreQuizState(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            return;
        }
        View selectedView = mQuizView.getSelectedView();
        if (selectedView instanceof ViewGroup) {
            ViewGroup selectedViewGroup = (ViewGroup) selectedView;
            View currentChild = selectedViewGroup.getChildAt(0);
            if (currentChild instanceof AbsQuizView) {
                ((AbsQuizView) currentChild).setUserInput(savedInstanceState.
                        getBundle(KEY_USER_INPUT));
            }
        }
    }

    private QuizAdapter getQuizAdapter() {
        if (null == mQuizAdapter) {
            mQuizAdapter = new QuizAdapter(getActivity(), mCategory);
        }
        return mQuizAdapter;
    }

    public boolean nextPage() {
        if (null == mQuizView) {
            return false;
        }
        int nextItem = mQuizView.getDisplayedChild() + 1;
        final int count = mQuizView.getAdapter().getCount();
        if (nextItem < count) {
            moveToNextItem();
            return true;
        }
        markCategorySolved();
        return false;
    }

    private void moveToNextItem() {
        mQuizView.showNext();
        TopekaDatabaseHelper.updateCategory(getActivity(), mCategory);
    }

    private void markCategorySolved() {
        mCategory.setSolved(true);
        TopekaDatabaseHelper.updateCategory(getActivity(), mCategory);
    }

    public void showSummary() {
        final ListView scorecardView = (ListView) getView().findViewById(R.id.scorecard);
        mScoreAdapter = getScoreAdapter();
        scorecardView.setAdapter(mScoreAdapter);
        scorecardView.setVisibility(View.VISIBLE);
        mQuizView.setVisibility(View.GONE);
    }

    private ScoreAdapter getScoreAdapter() {
        if (null == mScoreAdapter) {
            mScoreAdapter = new ScoreAdapter(mCategory);
        }
        return mScoreAdapter;
    }
}
