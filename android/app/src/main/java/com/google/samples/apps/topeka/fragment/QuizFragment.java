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
package com.google.samples.apps.topeka.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.adapter.QuizAdapter;
import com.google.samples.apps.topeka.adapter.ScoreAdapter;
import com.google.samples.apps.topeka.helper.PreferencesHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.model.Theme;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;
import com.google.samples.apps.topeka.widget.AvatarView;
import com.google.samples.apps.topeka.widget.quiz.AbsQuizView;

import java.util.List;

/**
 * Encapsulates Quiz solving and displays it to the user.
 */
public class QuizFragment extends Fragment {

    private static final String KEY_USER_INPUT = "USER_INPUT";
    private TextView mProgressText;
    private int mQuizSize;
    private ProgressBar mProgressBar;
    private Category mCategory;
    private AdapterViewAnimator mQuizView;
    private ScoreAdapter mScoreAdapter;
    private QuizAdapter mQuizAdapter;
    private SolvedStateListener mSolvedStateListener;

    public static QuizFragment newInstance(String categoryId,
            SolvedStateListener solvedStateListener) {
        if (categoryId == null) {
            throw new IllegalArgumentException("The category can not be null");
        }
        Bundle args = new Bundle();
        args.putString(Category.TAG, categoryId);
        QuizFragment fragment = new QuizFragment();
        if (solvedStateListener != null) {
            fragment.mSolvedStateListener = solvedStateListener;
        }
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
        // Create a themed Context and custom LayoutInflater
        // to get nicely themed views in this Fragment.
        final Theme theme = mCategory.getTheme();
        final ContextThemeWrapper context = new ContextThemeWrapper(getActivity(),
                theme.getStyleId());
        final LayoutInflater themedInflater = LayoutInflater.from(context);
        return themedInflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mQuizView = (AdapterViewAnimator) view.findViewById(R.id.quiz_view);
        decideOnViewToDisplay();
        mQuizView.setInAnimation(getActivity(), R.animator.slide_in_bottom);
        mQuizView.setOutAnimation(getActivity(), R.animator.slide_out_top);
        final AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
        setAvatarDrawable(avatar);
        initProgressToolbar(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initProgressToolbar(View view) {
        final int firstUnsolvedQuizPosition = mCategory.getFirstUnsolvedQuizPosition();
        final List<Quiz> quizzes = mCategory.getQuizzes();
        mQuizSize = quizzes.size();
        mProgressText = (TextView) view.findViewById(R.id.progress_text);
        mProgressBar = ((ProgressBar) view.findViewById(R.id.progress));
        mProgressBar.setMax(mQuizSize);

        setProgress(firstUnsolvedQuizPosition);
    }

    private void setProgress(int currentQuizPosition) {
        if (!isAdded()) {
            return;
        }
        mProgressText
                .setText(getString(R.string.quiz_of_quizzes, currentQuizPosition, mQuizSize));
        mProgressBar.setProgress(currentQuizPosition);
    }

    @SuppressWarnings("ConstantConditions")
    private void setAvatarDrawable(AvatarView avatarView) {
        Player player = PreferencesHelper.getPlayer(getActivity());
        avatarView.setImageResource(player.getAvatar().getDrawableId());
    }

    private void decideOnViewToDisplay() {
        final boolean isSolved = mCategory.isSolved();
        if (isSolved) {
            showSummary();
            if (null != mSolvedStateListener) {
                mSolvedStateListener.onCategorySolved();
            }
        } else {
            mQuizView.setAdapter(getQuizAdapter());
            mQuizView.setSelection(mCategory.getFirstUnsolvedQuizPosition());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        View focusedChild = mQuizView.getFocusedChild();
        if (focusedChild instanceof ViewGroup) {
            View currentView = ((ViewGroup) focusedChild).getChildAt(0);
            if (currentView instanceof AbsQuizView) {
                outState.putBundle(KEY_USER_INPUT, ((AbsQuizView) currentView).getUserInput());
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        restoreQuizState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    private void restoreQuizState(final Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            return;
        }
        mQuizView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                    int oldLeft,
                    int oldTop, int oldRight, int oldBottom) {
                mQuizView.removeOnLayoutChangeListener(this);
                View currentChild = mQuizView.getChildAt(0);
                if (currentChild instanceof ViewGroup) {
                    final View potentialQuizView = ((ViewGroup) currentChild).getChildAt(0);
                    if (potentialQuizView instanceof AbsQuizView) {
                        ((AbsQuizView) potentialQuizView).setUserInput(savedInstanceState.
                                getBundle(KEY_USER_INPUT));
                    }
                }
            }
        });

    }

    private QuizAdapter getQuizAdapter() {
        if (null == mQuizAdapter) {
            mQuizAdapter = new QuizAdapter(getActivity(), mCategory);
        }
        return mQuizAdapter;
    }

    /**
     * Displays the next page.
     *
     * @return <code>true</code> if there's another quiz to solve, else <code>false</code>.
     */
    public boolean showNextPage() {
        if (null == mQuizView) {
            return false;
        }
        int nextItem = mQuizView.getDisplayedChild() + 1;
        setProgress(nextItem);
        final int count = mQuizView.getAdapter().getCount();
        if (nextItem < count) {
            mQuizView.showNext();
            TopekaDatabaseHelper.updateCategory(getActivity(), mCategory);
            return true;
        }
        markCategorySolved();
        return false;
    }

    private void markCategorySolved() {
        mCategory.setSolved(true);
        TopekaDatabaseHelper.updateCategory(getActivity(), mCategory);
    }

    public void showSummary() {
        @SuppressWarnings("ConstantConditions")
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

    /**
     * Interface definition for a callback to be invoked when the quiz is started.
     */
    public interface SolvedStateListener {

        /**
         * This method will be invoked when the category has been solved.
         */
        void onCategorySolved();
    }
}