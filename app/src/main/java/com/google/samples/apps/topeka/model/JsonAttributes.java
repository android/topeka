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

    public static final String ANSWER = "answer";
    public static final String END = "end";
    public static final String ID = "id";
    public static final String MAX = "max";
    public static final String MIN = "min";
    public static final String NAME = "name";
    public static final String OPTIONS = "options";
    public static final String QUESTION = "question";
    public static final String QUIZZES = "quizzes";
    public static final String START = "start";
    public static final String STEP = "step";
    public static final String THEME = "theme";
    public static final String TYPE = "type";
    public static final String SCORES = "scores";
    public static final String SOLVED = "solved";

    public interface QuizType {

        public static final String ALPHA_PICKER = "alpha-picker";
        public static final String FILL_BLANK = "fill-blank";
        public static final String FILL_TWO_BLANKS = "fill-two-blanks";
        public static final String FOUR_QUARTER = "four-quarter";
        public static final String MULTI_SELECT = "multi-select";
        public static final String PICKER = "picker";
        public static final String SINGLE_SELECT = "single-select";
        public static final String SINGLE_SELECT_ITEM = "single-select-item";
        public static final String TOGGLE_TRANSLATE = "toggle-translate";
        public static final String TRUE_FALSE = "true-false";
    }
}
