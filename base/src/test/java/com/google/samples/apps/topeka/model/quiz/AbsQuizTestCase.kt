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
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * Removes redundancy during test creation for Quiz tests.

 * @param <Q> The Quiz type under test.
</Q> */
abstract class AbsQuizTestCase<out Q : Quiz<*>> {

    /**
     * Implementations need to check that the expected answer is equal
     * to the one returned from the quiz.
     */
    @Test
    abstract fun quiz_answer_correctlyStored()

    @Test
    abstract fun quiz_answer_isCorrect()

    /**
     * Implementations need to return a newly instantiated [Quiz] here.

     * @return The instantiated [Quiz].
     */
    abstract val quiz: Q

    /**
     * Implementations have to return the expected quiz type for the corresponding [Quiz].
     * This should **NOT** be obtained by calling getQuiz.getType()!

     * @return The expected [QuizType].
     */
    abstract val expectedQuizType: QuizType

    @Test fun getType_returnsExpectedType() = assertThat(expectedQuizType, `is`(quiz.type))

    @Test fun question_isStoredCorrectly() = assertThat(QUESTION, `is`(quiz.question))

    @Test fun quiz_answer_stringRepresentation_exists() = assertThat(quiz.stringAnswer, notNullValue())

    @Test fun quiz_equalsSelf_true() = assertThat(quiz == quiz, `is`(true))

    @Test fun quiz_equalsOther_false() {
        assertThat(quiz == TrueFalseQuiz("Foobar", false, false), `is`(false))
    }

    @Test fun quiz_hashCode_consistent() = assertThat(quiz.hashCode() == quiz.hashCode(), `is`(true))

}

val QUESTION = "Is this the real life? Is this just fantasy?"
val INT_ARRAY = intArrayOf(0, 1, 2)
val STRING_ARRAY = arrayOf("one", "two", "three", "four")
