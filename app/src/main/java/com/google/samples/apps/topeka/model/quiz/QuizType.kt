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

package com.google.samples.apps.topeka.model.quiz

/**
 * Available types of quizzes.
 * Maps [QuizType] to subclasses of [Quiz].
 */
enum class QuizType(val jsonName: String, val type: Class<out Quiz<*>>) {
    ALPHA_PICKER("alpha-picker", AlphaPickerQuiz::class.java),
    FILL_BLANK("fill-blank", FillBlankQuiz::class.java),
    FILL_TWO_BLANKS("fill-two-blanks", FillTwoBlanksQuiz::class.java),
    FOUR_QUARTER("four-quarter", FourQuarterQuiz::class.java),
    MULTI_SELECT("multi-select", MultiSelectQuiz::class.java),
    PICKER("picker", PickerQuiz::class.java),
    SINGLE_SELECT("single-select", SelectItemQuiz::class.java),
    SINGLE_SELECT_ITEM("single-select-item", SelectItemQuiz::class.java),
    TOGGLE_TRANSLATE("toggle-translate", ToggleTranslateQuiz::class.java),
    TRUE_FALSE("true-false", TrueFalseQuiz::class.java)
}