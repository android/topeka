/*
 * Copyright 2015 Google Inc. All Rights Reserved.
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

package com.google.samples.apps.topeka;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz;
import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz;
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz;
import com.google.samples.apps.topeka.model.quiz.OptionsQuiz;
import com.google.samples.apps.topeka.model.quiz.PickerQuiz;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz;
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz;

import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Utility for quiz solving.
 */
public class SolveQuizUtil {

    /**
     * Solves a given quiz.
     *
     * @param quiz Quiz to solve.
     */
    public static void solveQuiz(Quiz quiz) {
        switch (quiz.getType()) {
            case ALPHA_PICKER:
                setAlphaPickerProgress((AlphaPickerQuiz) quiz);
                break;
            case PICKER:
                setPickerProgress((PickerQuiz) quiz);
                break;
            case FILL_BLANK:
                FillBlankQuiz fillBlankQuiz = (FillBlankQuiz) quiz;
                String siblingText = fillBlankQuiz.getStart();
                if (TextUtils.isEmpty(siblingText)) {
                    siblingText = fillBlankQuiz.getEnd();
                }
                int viewId = R.id.quiz_edit_text;
                if (TextUtils.isEmpty(siblingText)) {
                    siblingText = quiz.getQuestion();
                    viewId = R.id.quiz_content;
                }
                typeAndCloseOnView(fillBlankQuiz.getAnswer(), siblingText, viewId);
                break;
            case FILL_TWO_BLANKS:
                FillTwoBlanksQuiz fillTwoBlanksQuiz = (FillTwoBlanksQuiz) quiz;
                typeAndCloseOnView(fillTwoBlanksQuiz.getAnswer()[0], R.id.quiz_edit_text);
                typeAndCloseOnView(fillTwoBlanksQuiz.getAnswer()[1], R.id.quiz_edit_text_two);
                break;
            case FOUR_QUARTER:
                testOptionsQuizWithType(quiz, GridView.class);
                break;
            case SINGLE_SELECT:
            case SINGLE_SELECT_ITEM:
            case MULTI_SELECT:
                testOptionsQuizWithType(quiz, ListView.class);
                break;
            case TOGGLE_TRANSLATE:
                ToggleTranslateQuiz toggleTranslateQuiz = (ToggleTranslateQuiz) quiz;
                for (int i : toggleTranslateQuiz.getAnswer()) {
                    onData(equalTo(toggleTranslateQuiz.getReadableOptions()[i]))
                            .inAdapterView(allOf(instanceOf(AdapterView.class),
                                    withId(R.id.quiz_content),
                                    hasSiblingWith(quiz.getQuestion())))
                            .perform(click());
                }
                break;
            case TRUE_FALSE:
                TrueFalseQuiz trueFalseQuiz = (TrueFalseQuiz) quiz;
                onView(allOf(isDescendantOfA(hasSibling(withText(quiz.getQuestion()))), withText(
                        trueFalseQuiz.getAnswer() ? R.string.btn_true : R.string.btn_false)))
                        .perform(click());
                break;
        }
    }

    private static void testOptionsQuizWithType(Quiz quiz, Class<? extends View> type) {
        @SuppressWarnings("unchecked")
        OptionsQuiz<String> stringOptionsQuiz = (OptionsQuiz<String>) quiz;
        for (int i : stringOptionsQuiz.getAnswer()) {
            onData(equalTo(stringOptionsQuiz.getOptions()[i]))
                    .inAdapterView(allOf(instanceOf(type),
                            withId(R.id.quiz_content),
                            hasSiblingWith(quiz.getQuestion())))
                    .perform(click());
        }
    }

    private static void setAlphaPickerProgress(final AlphaPickerQuiz quiz) {
        onView(allOf(isDescendantOfA(hasSibling(withText(quiz.getQuestion()))),
                withId(R.id.seekbar)))
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isAssignableFrom(SeekBar.class);
                    }

                    @Override
                    public String getDescription() {
                        return "Set progress on AlphaPickerQuizView";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        List<String> alphabet = Arrays.asList(InstrumentationRegistry.
                                getTargetContext()
                                .getResources()
                                .getStringArray(R.array.alphabet));

                        SeekBar seekBar = (SeekBar) view;
                        seekBar.setProgress(alphabet.indexOf(quiz.getAnswer()));
                    }
                });
    }

    private static void setPickerProgress(final PickerQuiz pickerQuiz) {
        onView(allOf(isDescendantOfA(hasSibling(withText(pickerQuiz.getQuestion()))),
                withId(R.id.seekbar)))
                .perform(click())
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isAssignableFrom(SeekBar.class);
                    }

                    @Override
                    public String getDescription() {
                        return "Set progress on PickerQuizView";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        SeekBar seekBar = (SeekBar) view;
                        seekBar.setProgress(pickerQuiz.getAnswer());
                    }
                });
    }

    private static void typeAndCloseOnView(String answer, String siblingText, int viewId) {
        onView(allOf(withId(viewId), hasSiblingWith(siblingText)))
                .perform(typeText(answer), closeSoftKeyboard());
    }

    private static void typeAndCloseOnView(String answer, int viewId) {
        onView(withId(viewId))
                .perform(typeText(answer), closeSoftKeyboard());
    }

    @NonNull
    private static Matcher<View> hasSiblingWith(String text) {
        return hasSibling(withText(text));
    }
}
