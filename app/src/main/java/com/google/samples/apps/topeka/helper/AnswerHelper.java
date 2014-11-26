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
package com.google.samples.apps.topeka.helper;

/**
 * Collection of methods to convert answers to human readable forms.
 */
public class AnswerHelper {

    private AnswerHelper() {
        //no instance
    }

    public static String getAnswer(String[] answers) {
        StringBuilder readableAnswer = new StringBuilder();
        //Iterate over all answers
        for (int i = 0; i < answers.length; i++) {
            String answer = answers[i];
            readableAnswer.append(answer);
            //Don't add a separator for the last answer
            if (i < answers.length - 1) {
                readableAnswer.append("\n");
            }
        }
        return readableAnswer.toString();
    }

    public static String getAnswer(int[] answers, String[] options) {
        String[] readableAnswers = new String[answers.length];
        for (int i = 0; i < answers.length; i++) {
            final String humanReadableAnswer = options[answers[i]];
            readableAnswers[i] = humanReadableAnswer;
        }
        return getAnswer(readableAnswers);
    }
}
