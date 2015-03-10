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

import android.util.SparseBooleanArray;

import junit.framework.TestCase;

public class AnswerHelperTest extends TestCase {

    private final String[] answers;
    private final int[] options;

    public AnswerHelperTest() {
        options = new int[]{0, 1, 2};
        answers = new String[]{"one", "two", "three"};
    }

    public void testGetAnswer() throws Exception {
        String answer = AnswerHelper.getAnswer(answers);
        assertNotNull(answer);
        assertFalse(answer.endsWith(AnswerHelper.SEPARATOR));
        for (String s : answers) {
            assertTrue(answer.contains(s));
        }
    }

    public void testGetAnswerWithOptions() throws Exception {
        String answer = AnswerHelper.getAnswer(options, answers);
        assertNotNull(answer);
        assertFalse(answer.endsWith(AnswerHelper.SEPARATOR));
        for (String s : answers) {
            assertTrue(answer.contains(s));
        }
    }

    public void testIsAnswerCorrect() throws Exception {
        SparseBooleanArray correctAnswer = new SparseBooleanArray();
        correctAnswer.put(0, true);
        correctAnswer.put(1, true);
        correctAnswer.put(2, true);
        assertFalse(AnswerHelper.isAnswerCorrect(correctAnswer, new int[]{2, 3}));
        assertTrue(AnswerHelper.isAnswerCorrect(correctAnswer, options));
    }
}