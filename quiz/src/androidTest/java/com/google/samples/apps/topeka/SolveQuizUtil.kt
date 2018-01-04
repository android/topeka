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

package com.google.samples.apps.topeka

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.hasSibling
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ListView
import android.widget.SeekBar
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz
import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz
import com.google.samples.apps.topeka.model.quiz.OptionsQuiz
import com.google.samples.apps.topeka.model.quiz.PickerQuiz
import com.google.samples.apps.topeka.model.quiz.Quiz
import com.google.samples.apps.topeka.model.quiz.QuizType
import com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.instanceOf
import java.util.Arrays


/**
 * Utility for quiz solving.
 */
object SolveQuizUtil {

    /**
     * Solves a given quiz.

     * @param quiz Quiz to solve.
     */
    fun solveQuiz(quiz: Quiz<*>) {
        when (quiz.type) {
            QuizType.ALPHA_PICKER -> setAlphaPickerProgress(quiz as AlphaPickerQuiz)
            QuizType.PICKER -> setPickerProgress(quiz as PickerQuiz)
            QuizType.FILL_BLANK -> {
                val fillBlankQuiz = quiz as FillBlankQuiz
                var siblingText: String? = fillBlankQuiz.start
                if (TextUtils.isEmpty(siblingText)) {
                    siblingText = fillBlankQuiz.end
                }

                var viewId = R.id.quiz_edit_text
                if (TextUtils.isEmpty(siblingText)) {
                    siblingText = quiz.question
                    viewId = R.id.quiz_content
                }
                typeAndCloseOnView(fillBlankQuiz.answer, siblingText, viewId)
            }
            QuizType.FILL_TWO_BLANKS -> {
                val (_, answer) = quiz as FillTwoBlanksQuiz
                typeAndCloseOnView(answer[0], R.id.quiz_edit_text)
                typeAndCloseOnView(answer[1], R.id.quiz_edit_text_two)
            }
            QuizType.FOUR_QUARTER -> testOptionsQuizWithType(quiz, GridView::class.java)
            QuizType.SINGLE_SELECT, QuizType.SINGLE_SELECT_ITEM, QuizType.MULTI_SELECT ->
                testOptionsQuizWithType(quiz, ListView::class.java)
            QuizType.TOGGLE_TRANSLATE -> {
                val toggleTranslateQuiz = quiz as ToggleTranslateQuiz
                toggleTranslateQuiz.answer.forEach {
                    onData(equalTo(toggleTranslateQuiz.readableOptions[it]))
                            .inAdapterView(allOf(instanceOf<Any>(AdapterView::class.java),
                                    withId(R.id.quiz_content),
                                    hasSiblingWith(quiz.question)))
                            .perform(click())
                }
            }
            QuizType.TRUE_FALSE -> {
                val (_, answer) = quiz as TrueFalseQuiz
                onView(allOf(isDescendantOfA(hasSibling(withText(quiz.question))), withText(
                        if (answer) R.string.btn_true else R.string.btn_false)))
                        .perform(click())
            }
        }
    }

    private fun testOptionsQuizWithType(quiz: Quiz<*>, type: Class<out View>) {
        val stringOptionsQuiz = quiz as OptionsQuiz<*>
        stringOptionsQuiz.answer.forEach {
            onData(equalTo(stringOptionsQuiz.options[it]))
                    .inAdapterView(allOf(instanceOf<Any>(type),
                            withId(R.id.quiz_content),
                            hasSiblingWith(quiz.question)))
                    .perform(click())
        }
    }

    private fun setAlphaPickerProgress(quiz: AlphaPickerQuiz) {
        onView(allOf(isDescendantOfA(hasSibling(withText(quiz.question))), withId(R.id.seekbar)))
                .perform(object : ViewAction {
                    override fun getConstraints(): Matcher<View>? {
                        return ViewMatchers.isAssignableFrom(SeekBar::class.java)
                    }

                    override fun getDescription() = "Set progress on AlphaPickerQuizView"

                    override fun perform(uiController: UiController, view: View) {
                        val alphabet = Arrays.asList(*InstrumentationRegistry.getTargetContext()
                                .resources
                                .getStringArray(R.array.alphabet))

                        (view as SeekBar).progress = alphabet.indexOf(quiz.answer)
                    }
                })
    }

    private fun setPickerProgress(pickerQuiz: PickerQuiz) {
        onView(allOf(isDescendantOfA(hasSibling(withText(pickerQuiz.question))),
                withId(R.id.seekbar)))
                .perform(click())
                .perform(object : ViewAction {
                    override fun getConstraints(): Matcher<View>? {
                        return isAssignableFrom(SeekBar::class.java)
                    }

                    override fun getDescription() = "Set progress on PickerQuizView"

                    override fun perform(uiController: UiController, view: View) {
                        (view as SeekBar).progress = pickerQuiz.answer
                    }
                })
    }

    private fun typeAndCloseOnView(answer: String?, siblingText: String?, viewId: Int) {
        onView(allOf(withId(viewId), hasSiblingWith(siblingText)))
                .perform(typeText(answer), closeSoftKeyboard())
    }

    private fun typeAndCloseOnView(answer: String?, viewId: Int) {
        onView(withId(viewId)).perform(typeText(answer), closeSoftKeyboard())
    }

    private fun hasSiblingWith(text: String?): Matcher<View> {
        return hasSibling(withText(text))
    }


    fun rotate() {
        val activity = getCurrentActivity()!!
        val currentOrientation = activity.resources.configuration.orientation

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) rotateToPortrait(activity)
        else rotateToLandscape(activity)
    }

    private fun rotateToLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun rotateToPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun getCurrentActivity(): Activity? {
        var resumedActivity: Activity? = null

        getInstrumentation().runOnMainSync {
            resumedActivity = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED).first()
        }
        return resumedActivity
    }
}
