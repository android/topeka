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
package com.google.samples.apps.topeka.widget.quiz;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Theme;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.widget.DoneFab;
import com.google.samples.apps.topeka.widget.FloatingActionButton;

/**
 * This is the base class for displaying a {@link com.google.samples.apps.topeka.model.quiz.Quiz}.
 * <p>
 * Subclasses need to implement {@link AbsQuizView#getQuizContentView()}
 * in order to allow solution of a quiz.
 * </p>
 * <p>
 * Also {@link AbsQuizView#setAnswered(boolean)} needs to be called with
 * <code>true</code> in order to mark the quiz solved.
 * </p>
 * @param <Q> The type of {@link com.google.samples.apps.topeka.model.quiz.Quiz} you want to
 *            display.
 */
public abstract class AbsQuizView<Q extends Quiz> extends CardView implements
        View.OnClickListener {

    private final TextView mQuestionView;
    private FloatingActionButton mSubmitAnswer;
    private Q mQuiz;
    private boolean mAnswered;

    public AbsQuizView(Context context, Category category, Q quiz) {
        super(context);
        mQuiz = quiz;
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        mQuestionView = new TextView(context);
        mQuestionView
                .setTextAppearance(getContext(), android.R.style.TextAppearance_Material_Subhead);
        setMinHeight(mQuestionView);
        setupQuestionView(category);

        View quizContentView = getQuizContentView();
        setDefaultPadding(quizContentView);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        container.addView(mQuestionView, layoutParams);
        container.addView(quizContentView, layoutParams);
        addView(container, layoutParams);
        mSubmitAnswer = getSubmitButton(context);
        final int fabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
        addView(mSubmitAnswer, new LayoutParams(fabSize,
                fabSize, Gravity.END));
    }

    private FloatingActionButton getSubmitButton(Context context) {
        if (null == mSubmitAnswer) {
            mSubmitAnswer = new DoneFab(context);
            mSubmitAnswer.setId(R.id.submitAnswer);
            mSubmitAnswer.setVisibility(GONE);
        }
        return mSubmitAnswer;
    }

    private void setupQuestionView(Category category) {
        mQuestionView.setText(getQuiz().getQuestion());
        mQuestionView
                .setTextAppearance(getContext(), android.R.style.TextAppearance_Material_Subhead);
        mQuestionView.setTextColor(category.getTheme().getTextPrimaryColor());
        int backgroundColor = getResources().getColor(category.getTheme().getPrimaryColor());
        mQuestionView.setBackgroundColor(backgroundColor);
    }

    private void setDefaultPadding(View view) {
        final int padding = getResources().getDimensionPixelSize(R.dimen.padding_default);
        view.setPadding(padding, padding, padding, padding);
    }

    /**
     * Implementations should create the content view for the type of
     * {@link com.google.samples.apps.topeka.model.quiz.Quiz} they want to display.
     *
     * @return the created view to solve the quiz.
     */
    protected abstract View getQuizContentView();

    public Q getQuiz() {
        return mQuiz;
    }

    protected boolean isAnswered() {
        return mAnswered;
    }

    /**
     * Sets the quiz to answered or unanswered.
     *
     * @param answered <code>true</code> if an answer was selected, else <code>false</code>.
     */
    protected void setAnswered(boolean answered) {
        if (answered) {
            mSubmitAnswer.setVisibility(View.VISIBLE);
        } else {
            mSubmitAnswer.setVisibility(View.GONE);
        }
        mAnswered = answered;
    }

    /**
     * Sets the quiz to answered if it not already has been answered.
     * Otherwise does nothing.
     */
    protected void answerQuiz() {
        if (!isAnswered()) {
            setAnswered(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitAnswer: {
                //TODO: 11/3/14 handle
                break;
            }
        }
    }

    protected void setMinHeight(View view) {
        view.setMinimumHeight(
                getResources().getDimensionPixelSize(R.dimen.min_height_touch_target));
    }
}
