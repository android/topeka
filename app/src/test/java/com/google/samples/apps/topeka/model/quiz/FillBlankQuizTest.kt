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

class FillBlankQuizTest : AbsQuizTestCase<FillBlankQuiz>() {

    private val ANSWER = "answer"
    private val START = "start"
    private val END = "end"

    override val quiz get() = FillBlankQuiz(QUESTION, ANSWER, START, END, false)

    override val expectedQuizType get() = QuizType.FILL_BLANK

    override fun quiz_answer_isCorrect() = assertThat(quiz.isAnswerCorrect(quiz.answer), `is`(true))

    override fun quiz_answer_correctlyStored() = assertThat(ANSWER, `is`(quiz.answer))

    @Test fun start_correctlyStored() = assertThat(START, `is`(quiz.start))

    @Test fun end_correctlyStored() = assertThat(END, `is`(quiz.end))


}