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
package com.google.samples.apps.topeka.model.quiz;

import static com.google.samples.apps.topeka.model.JsonAttributes.QuizName;

/**
 * Available types of quizzes.
 * Maps {@link QuizName} to subclasses of {@link Quiz}.
 */
public enum QuizType {
    ALPHA_PICKER(QuizName.ALPHA_PICKER, AlphaPickerQuiz.class),
    FILL_BLANK(QuizName.FILL_BLANK, FillBlankQuiz.class),
    FILL_TWO_BLANKS(QuizName.FILL_TWO_BLANKS, FillTwoBlanksQuiz.class),
    FOUR_QUARTER(QuizName.FOUR_QUARTER, FourQuarterQuiz.class),
    MULTI_SELECT(QuizName.MULTI_SELECT, MultiSelectQuiz.class),
    PICKER(QuizName.PICKER, PickerQuiz.class),
    SINGLE_SELECT(QuizName.SINGLE_SELECT, SelectItemQuiz.class),
    SINGLE_SELECT_ITEM(QuizName.SINGLE_SELECT_ITEM, SelectItemQuiz.class),
    TOGGLE_TRANSLATE(QuizName.TOGGLE_TRANSLATE, ToggleTranslateQuiz.class),
    TRUE_FALSE(QuizName.TRUE_FALSE, TrueFalseQuiz.class);

    private final String mJsonName;
    private final Class<? extends Quiz> mType;

    QuizType(final String jsonName, final Class<? extends Quiz> type) {
        mJsonName = jsonName;
        mType = type;
    }

    public String getJsonName() {
        return mJsonName;
    }

    public Class<? extends Quiz> getType() {
        return mType;
    }
}
