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

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
public class AnswerHelperTest {

    private static final String[] ANSWERS = new String[]{"one", "two", "three"};
    private static final int[] OPTIONS = new int[]{0, 1, 2};

    @Test
    public void getAnswer_notNull() {
        String answer = AnswerHelper.getAnswer(ANSWERS);
        assertThat(answer, notNullValue());
    }

    @Test
    public void getAnswer_allContained() {
        String answer = AnswerHelper.getAnswer(ANSWERS);
        for (String s : ANSWERS) {
            assertThat(answer.contains(s), is(true));
        }
    }

    @Test
    public void getAnswer_multipleAnswers_dontEndWithSeparator() {
        String answer = AnswerHelper.getAnswer(ANSWERS);
        assertThat(answer.endsWith(AnswerHelper.SEPARATOR), is(false));
    }

    @Test
    public void getAnswer_withOptions_allContained() {
        String answer = AnswerHelper.getAnswer(OPTIONS, ANSWERS);
        for (String s : ANSWERS) {
            assertThat(answer.contains(s), is(true));
        }
    }

    @Test
    public void getAnswer_withOptions_dontEndWithSeparator() {
        String answer = AnswerHelper.getAnswer(OPTIONS, ANSWERS);
        assertThat(answer.endsWith(AnswerHelper.SEPARATOR), is(false));
    }

    @Test
    public void getAnswer_withOptions_notNull() {
        String answer = AnswerHelper.getAnswer(OPTIONS, ANSWERS);
        assertThat(answer, notNullValue());
    }
}