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

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
public class PickerQuizTest extends AbsQuizTestCase<PickerQuiz> {

    private static final Integer ANSWER = 100;
    private static final int MIN = 1;
    private static final int MAX = 1000;
    private static final int STEP = 10;

    @Test
    public void min_correctlyStored() {
        assertThat(MIN, is(getQuiz().getMin()));
    }

    @Test
    public void max_correctlyStored() {
        assertThat(MAX, is(getQuiz().getMax()));
    }

    @Test
    public void step_correctlyStored() {
        assertThat(STEP, is(getQuiz().getStep()));
    }

    @Override
    public void quiz_answer_correctlyStored() {
        assertThat(ANSWER, is(getQuiz().getAnswer()));
    }

    @Override
    public PickerQuiz getQuiz() {
        return new PickerQuiz(QUESTION, ANSWER, MIN, MAX, STEP, false);
    }

    @Override
    public QuizType getExpectedQuizType() {
        return QuizType.PICKER;
    }
}