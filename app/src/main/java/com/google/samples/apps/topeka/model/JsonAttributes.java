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

package com.google.samples.apps.topeka.model;

public interface JsonAttributes {

    String ANSWER = "answer";
    String END = "end";
    String ID = "id";
    String MAX = "max";
    String MIN = "min";
    String NAME = "name";
    String OPTIONS = "options";
    String QUESTION = "question";
    String QUIZZES = "quizzes";
    String START = "start";
    String STEP = "step";
    String THEME = "theme";
    String TYPE = "type";
    String SCORES = "scores";
    String SOLVED = "solved";

    interface QuizType {

        String ALPHA_PICKER = "alpha-picker";
        String FILL_BLANK = "fill-blank";
        String FILL_TWO_BLANKS = "fill-two-blanks";
        String FOUR_QUARTER = "four-quarter";
        String MULTI_SELECT = "multi-select";
        String PICKER = "picker";
        String SINGLE_SELECT = "single-select";
        String SINGLE_SELECT_ITEM = "single-select-item";
        String TOGGLE_TRANSLATE = "toggle-translate";
        String TRUE_FALSE = "true-false";
    }
}
