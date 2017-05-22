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
package com.google.samples.apps.topeka.model.quiz

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PickerQuizTest : AbsQuizTestCase<PickerQuiz>() {

    private val ANSWER = 100
    private val MIN = 1
    private val MAX = 1000
    private val STEP = 10

    override val quiz get() = PickerQuiz(QUESTION, ANSWER, MIN, MAX, STEP, false)

    override val expectedQuizType get() = QuizType.PICKER

    override fun quiz_answer_isCorrect() = assertThat(quiz.isAnswerCorrect(quiz.answer), `is`(true))

    override fun quiz_answer_correctlyStored() = assertThat(ANSWER, `is`(quiz.answer))

    @Test fun min_correctlyStored() = assertThat(MIN, `is`(quiz.min))

    @Test fun max_correctlyStored() = assertThat(MAX, `is`(quiz.max))

    @Test fun step_correctlyStored() = assertThat(STEP, `is`(quiz.step))
}