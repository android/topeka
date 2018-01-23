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

import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import android.util.SparseBooleanArray
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests whether answers are being correctly stored and retrieved.
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
class AnswerHelperAndroidTest {
    private val ANSWER = intArrayOf(0, 1, 2)
    private lateinit var correctAnswer: SparseBooleanArray
    private lateinit var wrongAnswer: SparseBooleanArray

    @Before fun setUpAnswers() {

        correctAnswer = SparseBooleanArray().apply {
            put(0, true)
            put(1, true)
            put(2, true)
        }

        wrongAnswer = SparseBooleanArray().apply {
            put(0, true)
            put(1, true)
        }
    }

    @Test fun partial_isAnswerCorrect_returnsTrue() {
        assertThat(AnswerHelper.isAnswerCorrect(correctAnswer, ANSWER), `is`(true))
    }

    @Test fun partial_isAnswerCorrect_returnsFalse() {
        assertThat(AnswerHelper.isAnswerCorrect(wrongAnswer, ANSWER), `is`(false))
    }

}