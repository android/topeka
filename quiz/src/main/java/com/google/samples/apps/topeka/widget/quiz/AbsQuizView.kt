/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.topeka.widget.quiz

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.MarginLayoutParamsCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.Property
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.google.samples.apps.topeka.activity.QuizActivity
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.helper.FOREGROUND_COLOR
import com.google.samples.apps.topeka.helper.onLayoutChange
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.Quiz
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.widget.fab.CheckableFab

const val KEY_ANSWER = "answer"

/**
 * This is the base class for displaying a [com.google.samples.apps.topeka.model.quiz.Quiz].
 *
 *
 * Subclasses need to implement [AbsQuizView.createQuizContentView]
 * in order to allow solution of a quiz.
 *
 *
 *
 * Also [AbsQuizView.allowAnswer] needs to be called with
 * `true` in order to mark the quiz solved.
 *

 * @param <Q> The type of [com.google.samples.apps.topeka.model.quiz.Quiz] you want to
 * display.
</Q> */
abstract class AbsQuizView<out Q : Quiz<*>>
/**
 * Enables creation of views for quizzes.

 * @param context The context for this view.
 *
 * @param category The [Category] this view is running in.
 *
 * @param quiz The actual [Quiz] that is going to be displayed.
 */
(context: Context, private val category: Category, val quiz: Q) : FrameLayout(context) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    val quizContentView: View?

    init {
        id = quiz.id
        val container = createContainerLayout(context)

        quizContentView = createQuizContentView()?.apply {
            id = com.google.samples.apps.topeka.base.R.id.quiz_content
            isSaveEnabled = true
            if (this is ViewGroup) {
                clipToPadding = false
            }
            setMinHeightInternal(this)
            addContentView(container, this)
        }
        onLayoutChange { addFloatingActionButton() }
    }

    private val ANSWER_HIDE_DELAY = 500L

    private val FOREGROUND_COLOR_CHANGE_DELAY = 750L
    private val doubleSpacing = resources.getDimensionPixelSize(
            com.google.samples.apps.topeka.base.R.dimen.spacing_double)
    private val linearOutSlowInInterpolator = LinearOutSlowInInterpolator()
    private val quizHandler = Handler()

    private val submitAnswer = initCheckableFab()

    private fun initCheckableFab(): CheckableFab {
        return (inflate<CheckableFab>(R.layout.answer_submit)).apply {
            hide()
            setOnClickListener {
                submitAnswer()
                if (inputMethodManager.isAcceptingText) {
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                }
                isEnabled = false
            }
        }
    }

    private val inputMethodManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    protected var isAnswered = false
        private set

    /**
     * Implementations must make sure that the answer provided is evaluated and correctly rated.

     * @return `true` if the question has been correctly answered, else
     * `false`.
     */
    protected abstract val isAnswerCorrect: Boolean

    /**
     * Holds the user input for orientation change purposes.
     */
    abstract var userInput: Bundle

    private fun createContainerLayout(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            id = com.google.samples.apps.topeka.base.R.id.absQuizViewContainer
            orientation = LinearLayout.VERTICAL
        }
    }

    private fun addContentView(container: LinearLayout, quizContentView: View) {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
        container.run {
            addView((layoutInflater.inflate(R.layout.question, this, false) as TextView).apply {
                setBackgroundColor(ContextCompat.getColor(context,
                        category.theme.primaryColor))
                text = quiz.question
            }, layoutParams)
            addView(quizContentView, layoutParams)
        }
        addView(container, layoutParams)
    }

    private fun addFloatingActionButton() {
        val fabSize = resources.getDimensionPixelSize(
                com.google.samples.apps.topeka.base.R.dimen.size_fab)
        val bottomOfQuestionView = findViewById<View>(R.id.question_view).bottom
        val fabLayoutParams = FrameLayout.LayoutParams(fabSize, fabSize, Gravity.END or Gravity.TOP)
        val halfAFab = fabSize / 2
        fabLayoutParams.setMargins(0, // left
                bottomOfQuestionView - halfAFab, // top
                0, // right
                doubleSpacing) // bottom
        MarginLayoutParamsCompat.setMarginEnd(fabLayoutParams, doubleSpacing)
        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            // Account for the fab's emulated shadow.
            fabLayoutParams.topMargin -= submitAnswer.paddingTop / 2
        }
        addView(submitAnswer, fabLayoutParams)
    }

    /**
     * Implementations should create the content view for the type of
     * [com.google.samples.apps.topeka.model.quiz.Quiz] they want to display.

     * @return the created view to solve the quiz.
     */
    protected abstract fun createQuizContentView(): View?

    /**
     * Sets the quiz to answered or unanswered.

     * @param answered `true` if an answer was selected, else `false`.
     */
    protected fun allowAnswer(answered: Boolean = true) {
        with(submitAnswer) { if (answered) show() else hide() }
        isAnswered = answered
    }

    /**
     * Allows children to submit an answer via code.
     */
    protected open fun submitAnswer() {
        quiz.solved = true
        performScoreAnimation(isAnswerCorrect)
    }

    /**
     * Animates the view when the answer has been submitted.

     * @param answerCorrect `true` if the answer was correct, else `false`.
     */
    private fun performScoreAnimation(answerCorrect: Boolean) {
        (context as QuizActivity).lockIdlingResource()
        // Decide which background color to use.
        val backgroundColor = ContextCompat.getColor(context,
                if (answerCorrect) com.google.samples.apps.topeka.base.R.color.green
                else com.google.samples.apps.topeka.base.R.color.red)
        adjustFab(answerCorrect, backgroundColor)
        resizeView()
        moveViewOffScreen(answerCorrect)
        // Animate the foreground color to match the background color.
        // This overlays all content within the current view.
        animateForegroundColor(backgroundColor)
    }

    @SuppressLint("NewApi")
    private fun adjustFab(answerCorrect: Boolean, backgroundColor: Int) {
        with(submitAnswer) {
            isChecked = answerCorrect
            backgroundTintList = ColorStateList.valueOf(backgroundColor)
        }
        quizHandler.postDelayed({ submitAnswer.hide() }, ANSWER_HIDE_DELAY)
    }

    private fun resizeView() {
        val widthHeightRatio = height.toFloat() / width.toFloat()
        // Animate X and Y scaling separately to allow different start delays.
        // object animators for x and y with different durations and then run them independently
        resizeViewProperty(View.SCALE_X, .5f, 200)
        resizeViewProperty(View.SCALE_Y, .5f / widthHeightRatio, 300)
    }

    private fun resizeViewProperty(property: Property<View, Float>,
                                   targetScale: Float, durationOffset: Int) {
        with(ObjectAnimator.ofFloat(this, property, 1f, targetScale)) {
            interpolator = linearOutSlowInInterpolator
            startDelay = (FOREGROUND_COLOR_CHANGE_DELAY + durationOffset)
            start()
        }
    }

    override fun onDetachedFromWindow() {
        quizHandler.removeCallbacksAndMessages(null)
        super.onDetachedFromWindow()
    }

    private fun animateForegroundColor(@ColorInt targetColor: Int) {
        with(ObjectAnimator.ofInt(this, FOREGROUND_COLOR, Color.TRANSPARENT, targetColor)) {
            setEvaluator(ArgbEvaluator())
            startDelay = FOREGROUND_COLOR_CHANGE_DELAY
            start()
        }
    }

    private fun moveViewOffScreen(answerCorrect: Boolean) {
        category.setScore(quiz, answerCorrect)
        // Move the current view off the screen.
        quizHandler.postDelayed(this::proceed, FOREGROUND_COLOR_CHANGE_DELAY * 2)
    }

    private fun setMinHeightInternal(view: View) {
        view.minimumHeight = resources.getDimensionPixelSize(
                com.google.samples.apps.topeka.base.R.dimen.min_height_question)
    }

    private fun proceed() {
        if (context is QuizActivity) (context as QuizActivity).proceed()
    }

    protected fun <T: View> inflate(@LayoutRes resId: Int) =
            layoutInflater.inflate(resId, this, false) as T

}