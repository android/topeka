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

package com.google.samples.apps.topeka.helper;

import android.util.Log;
import android.util.SparseBooleanArray;

/**
 * Collection of methods to convert answers to human readable forms.
 */
public class AnswerHelper {

    static final String SEPARATOR = System.getProperty("line.separator");
    private static final String TAG = "AnswerHelper";

    private AnswerHelper() {
        //no instance
    }

    /**
     * Converts an array of answers to a readable answer.
     *
     * @param answers The answers to display.
     * @return The readable answer.
     */
    public static String getAnswer(String[] answers) {
        StringBuilder readableAnswer = new StringBuilder();
        //Iterate over all answers
        for (int i = 0; i < answers.length; i++) {
            String answer = answers[i];
            readableAnswer.append(answer);
            //Don't add a separator for the last answer
            if (i < answers.length - 1) {
                readableAnswer.append(SEPARATOR);
            }
        }
        return readableAnswer.toString();
    }

    /**
     * Converts an array of answers with options to a readable answer.
     *
     * @param answers The actual answers
     * @param options The options to display.
     * @return The readable answer.
     */
    public static String getAnswer(int[] answers, String[] options) {
        String[] readableAnswers = new String[answers.length];
        for (int i = 0; i < answers.length; i++) {
            final String humanReadableAnswer = options[answers[i]];
            readableAnswers[i] = humanReadableAnswer;
        }
        return getAnswer(readableAnswers);
    }

    /**
     * Checks whether a provided answer is correct.
     *
     * @param checkedItems The items that were selected.
     * @param answerIds The actual correct answer ids.
     * @return <code>true</code> if correct else <code>false</code>.
     */
    public static boolean isAnswerCorrect(SparseBooleanArray checkedItems, int[] answerIds) {
        if (null == checkedItems || null == answerIds) {
            Log.i(TAG, "isAnswerCorrect got a null parameter input.");
            return false;
        }
        for (int answer : answerIds) {
            if (0 > checkedItems.indexOfKey(answer)) {
                return false;
            }
        }
        return checkedItems.size() == answerIds.length;
    }

}
