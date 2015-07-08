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
package com.google.samples.apps.topeka.widget.quiz;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.util.IntProperty;
import android.util.Property;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.QuizActivity;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.widget.fab.CheckableFab;

/**
 * This is the base class for displaying a {@link com.google.samples.apps.topeka.model.quiz.Quiz}.
 * <p>
 * Subclasses need to implement {@link AbsQuizView#createQuizContentView()}
 * in order to allow solution of a quiz.
 * </p>
 * <p>
 * Also {@link AbsQuizView#allowAnswer(boolean)} needs to be called with
 * <code>true</code> in order to mark the quiz solved.
 * </p>
 *
 * @param <Q> The type of {@link com.google.samples.apps.topeka.model.quiz.Quiz} you want to
 * display.
 */
public abstract class AbsQuizView<Q extends Quiz> extends FrameLayout {

    /** Property for animating the foreground color */
    public static final Property<FrameLayout, Integer> FOREGROUND_COLOR =
            new IntProperty<FrameLayout>("foregroundColor") {

                @Override
                public void setValue(FrameLayout object, int value) {
                    if (object.getForeground() instanceof ColorDrawable) {
                        ((ColorDrawable) object.getForeground()).setColor(value);
                    } else {
                        object.setForeground(new ColorDrawable(value));
                    }
                }

                @Override
                public Integer get(FrameLayout object) {
                    return ((ColorDrawable) object.getForeground()).getColor();
                }
            };

    protected final int mMinHeightTouchTarget;
    private final int mSpacingDouble;
    private final LayoutInflater mLayoutInflater;
    private final Category mCategory;
    private final Q mQuiz;
    private final Interpolator mFastOutSlowInInterpolator;
    private final Interpolator mLinearOutSlowInInterpolator;
    private final int mColorAnimationDuration;
    private final int mIconAnimationDuration;
    private final int mScaleAnimationDuration;
    private boolean mAnswered;
    private TextView mQuestionView;
    private CheckableFab mSubmitAnswer;

    /**
     * Enables creation of views for quizzes.
     *
     * @param context The context for this view.
     * @param category The {@link Category} this view is running in.
     * @param quiz The actual {@link Quiz} that is going to be displayed.
     */
    public AbsQuizView(Context context, Category category, Q quiz) {
        super(context);
        mQuiz = quiz;
        mCategory = category;
        mSpacingDouble = getResources().getDimensionPixelSize(R.dimen.spacing_double);
        mSubmitAnswer = getSubmitButton(context);
        mLayoutInflater = LayoutInflater.from(context);
        mMinHeightTouchTarget = getResources()
                .getDimensionPixelSize(R.dimen.min_height_touch_target);
        mFastOutSlowInInterpolator = AnimationUtils
                .loadInterpolator(getContext(), android.R.interpolator.fast_out_slow_in);
        mLinearOutSlowInInterpolator = AnimationUtils
                .loadInterpolator(getContext(), android.R.interpolator.linear_out_slow_in);
        mColorAnimationDuration = 400;
        mIconAnimationDuration = 300;
        mScaleAnimationDuration = 200;

        setId(quiz.getId());
        setUpQuestionView();
        LinearLayout container = createContainerLayout(context);
        View quizContentView = getInitializedContentView();
        addContentView(container, quizContentView);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                    int oldLeft,
                    int oldTop, int oldRight, int oldBottom) {
                removeOnLayoutChangeListener(this);
                addFloatingActionButton();
            }
        });
    }

    /**
     * Sets the behaviour for all question views.
     */
    private void setUpQuestionView() {
        mQuestionView = (TextView) mLayoutInflater.inflate(R.layout.question, this, false);
        mQuestionView.setText(getQuiz().getQuestion());
    }

    private LinearLayout createContainerLayout(Context context) {
        LinearLayout container = new LinearLayout(context);
        container.setId(R.id.absQuizViewContainer);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private View getInitializedContentView() {
        View quizContentView = createQuizContentView();
        quizContentView.setId(R.id.quiz_content);
        quizContentView.setSaveEnabled(true);
        setDefaultPadding(quizContentView);
        if (quizContentView instanceof ViewGroup) {
            ((ViewGroup) quizContentView).setClipToPadding(false);
        }
        setMinHeightInternal(quizContentView, R.dimen.min_height_question);
        return quizContentView;
    }

    private void addContentView(LinearLayout container, View quizContentView) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        container.addView(mQuestionView, layoutParams);
        container.addView(quizContentView, layoutParams);
        addView(container, layoutParams);
    }

    private void addFloatingActionButton() {
        final int fabSize = getResources().getDimensionPixelSize(R.dimen.size_fab);
        int bottomOfQuestionView = findViewById(R.id.question_view).getBottom();
        final LayoutParams fabLayoutParams = new LayoutParams(fabSize, fabSize,
                Gravity.END | Gravity.TOP);
        final int halfAFab = fabSize / 2;
        fabLayoutParams.setMargins(0, // left
                bottomOfQuestionView - halfAFab, //top
                0, // right
                mSpacingDouble); // bottom
        fabLayoutParams.setMarginEnd(mSpacingDouble);
        addView(mSubmitAnswer, fabLayoutParams);
    }

    private CheckableFab getSubmitButton(Context context) {
        if (null == mSubmitAnswer) {
            mSubmitAnswer = new CheckableFab(context);
            mSubmitAnswer.setId(R.id.submitAnswer);
            mSubmitAnswer.setVisibility(GONE);
            mSubmitAnswer.setScaleY(0);
            mSubmitAnswer.setScaleX(0);
            mSubmitAnswer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitAnswer(v);
                }
            });
        }
        return mSubmitAnswer;
    }

    private void setDefaultPadding(View view) {
        view.setPadding(mSpacingDouble, mSpacingDouble, mSpacingDouble, mSpacingDouble);
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    /**
     * Implementations should create the content view for the type of
     * {@link com.google.samples.apps.topeka.model.quiz.Quiz} they want to display.
     *
     * @return the created view to solve the quiz.
     */
    protected abstract View createQuizContentView();

    /**
     * Implementations must make sure that the answer provided is evaluated and correctly rated.
     *
     * @return <code>true</code> if the question has been correctly answered, else
     * <code>false</code>.
     */
    protected abstract boolean isAnswerCorrect();

    /**
     * Save the user input to a bundle for orientation changes.
     *
     * @return The bundle containing the user's input.
     */
    public abstract Bundle getUserInput();

    /**
     * Restore the user's input.
     *
     * @param savedInput The input that the user made in a prior instance of this view.
     */
    public abstract void setUserInput(Bundle savedInput);

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
    protected void allowAnswer(final boolean answered) {
        if (null != mSubmitAnswer) {
            final float targetScale = answered ? 1f : 0f;
            if (answered) {
                mSubmitAnswer.setVisibility(View.VISIBLE);
            }
            mSubmitAnswer.animate().scaleX(targetScale).scaleY(targetScale)
                    .setInterpolator(mFastOutSlowInInterpolator);
            mAnswered = answered;
        }
    }

    /**
     * Sets the quiz to answered if it not already has been answered.
     * Otherwise does nothing.
     */
    protected void allowAnswer() {
        if (!isAnswered()) {
            allowAnswer(true);
        }
    }

    /**
     * Allows children to submit an answer via code.
     */
    protected void submitAnswer() {
        submitAnswer(findViewById(R.id.submitAnswer));
    }


    @SuppressWarnings("UnusedParameters")
    private void submitAnswer(final View v) {
        final boolean answerCorrect = isAnswerCorrect();
        mQuiz.setSolved(true);
        performScoreAnimation(answerCorrect);
    }

    /**
     * Animates the view nicely when the answer has been submitted.
     *
     * @param answerCorrect <code>true</code> if the answer was correct, else <code>false</code>.
     */
    private void performScoreAnimation(final boolean answerCorrect) {

        mSubmitAnswer.setChecked(answerCorrect);

        // Decide which background color to use.
        final int backgroundColor = getResources()
                .getColor(answerCorrect ? R.color.green : R.color.red);
        animateFabBackgroundColor(backgroundColor);
        hideFab();
        resizeView();
        moveViewOffScreen(answerCorrect);
        // Animate the foreground color to match the background color.
        // This overlays all content within the current view.
        animateForegroundColor(backgroundColor);
    }

    private void hideFab() {
        mSubmitAnswer.animate()
                .setDuration(mScaleAnimationDuration)
                .setStartDelay(mIconAnimationDuration * 2)
                .scaleX(0f)
                .scaleY(0f)
                .setInterpolator(mLinearOutSlowInInterpolator)
                .start();
    }

    private void resizeView() {
        final float widthHeightRatio = (float) getHeight() / (float) getWidth();

        // Animate X and Y scaling separately to allow different start delays.
        animate()
                .scaleY(.5f / widthHeightRatio)
                .setDuration(300)
                .setStartDelay(750)
                .start();
        animate()
                .scaleX(.5f)
                .setDuration(300)
                .setStartDelay(800)
                .start();
    }

    private void animateFabBackgroundColor(int backgroundColor) {
        // Set color, duration and interpolator for the color change. Then start the animation.
        final ObjectAnimator fabColorAnimator = ObjectAnimator
                .ofArgb(mSubmitAnswer, "backgroundColor", Color.WHITE, backgroundColor);
        fabColorAnimator.setDuration(mColorAnimationDuration)
                .setInterpolator(mFastOutSlowInInterpolator);
        fabColorAnimator.start();
    }

    private void animateForegroundColor(int targetColor) {
        final ObjectAnimator foregroundAnimator = ObjectAnimator
                .ofArgb(this, FOREGROUND_COLOR, Color.WHITE, targetColor);
        foregroundAnimator
                .setDuration(200)
                .setInterpolator(mLinearOutSlowInInterpolator);
        foregroundAnimator.setStartDelay(750);
        foregroundAnimator.start();
    }

    private void moveViewOffScreen(final boolean answerCorrect) {
        // Animate the current view off the screen.
        animate()
                .setDuration(200)
                .setStartDelay(1200)
                .setInterpolator(mLinearOutSlowInInterpolator)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mCategory.setScore(getQuiz(), answerCorrect);
                        if (getContext() instanceof QuizActivity) {
                            ((QuizActivity) getContext()).proceed();
                        }
                    }
                })
                .start();
    }

    private void setMinHeightInternal(View view, @DimenRes int resId) {
        view.setMinimumHeight(getResources().getDimensionPixelSize(resId));
    }
}
