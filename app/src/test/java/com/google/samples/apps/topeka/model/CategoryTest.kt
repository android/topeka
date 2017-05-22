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

package com.google.samples.apps.topeka.model

import com.google.samples.apps.topeka.model.quiz.Quiz
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class CategoryTest {

    @Test fun construct_withScores_sameSizeQuizAndScores() {
        assertThat(Category(NAME, ID, THEME, QUIZZES, SCORES, false), notNullValue())
    }

    @Test(expected = IllegalArgumentException::class)
    fun construct_withScores_failsDifferentSizeQuizAndScores() {
        Category(NAME, ID, THEME, QUIZZES, IntArray(2), false)
    }

    @Test fun hashCode_failsForDifferent() {
        createCategory().apply {
            val other = Category(ID, NAME, THEME, QUIZZES, SCORES, true)
            assertThat(hashCode() == other.hashCode(), `is`(false))
        }
    }

    private fun createCategory() = Category(NAME, ID, THEME, QUIZZES, SCORES, false)

    private val NAME = "Foo"
    private val ID = "id"
    private val THEME = Theme.blue
    private val QUIZZES = ArrayList<Quiz<*>>()
    private val SCORES = IntArray(0)

}