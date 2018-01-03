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
package com.google.samples.apps.topeka.helper

import com.google.samples.apps.topeka.model.quiz.STRING_ARRAY
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class AnswerHelperTest {


    @Test fun getAnswer_notNull() {
        assertThat(AnswerHelper.getReadableAnswer(STRING_ARRAY), notNullValue())
    }

    @Test fun getAnswer_allContained() {
        for (it in STRING_ARRAY) {
            assertThat(AnswerHelper.getReadableAnswer(STRING_ARRAY).contains(it), `is`(true))
        }
    }

    @Test fun getAnswer_multipleAnswers_dontEndWithSeparator() {
        assertThat(AnswerHelper.getReadableAnswer(STRING_ARRAY).endsWith(AnswerHelper.SEPARATOR),
                `is`(false))
    }
}