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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Removes redundancy during test creation for Quiz tests.
 *
 * @param <Q> The Quiz type under test.
 */
public abstract class AbsQuizTestCase<Q extends Quiz> {

    protected static final String QUESTION = "Is this the real life? Is this just fantasy?";
    protected static final int[] INT_ARRAY = new int[]{0, 1, 2};
    protected static final String[] STRING_ARRAY = new String[]{"one", "two", "three", "four"};

    @Test
    public void getType_returnsExpectedType() throws Exception {
        assertThat(getExpectedQuizType(), is(getQuiz().getType()));
    }

    @Test
    public void question_isStoredCorrectly() throws Exception {
        assertThat(QUESTION, is(getQuiz().getQuestion()));
    }

    @Test
    public void quiz_answer_stringRepresentation_exists() {
        assertThat(getQuiz().getStringAnswer(), notNullValue());
    }

    @Test
    public void quiz_answer_isCorrect() {
        assertThat(getQuiz().isAnswerCorrect(getQuiz().getAnswer()), is(true));
    }

    @Test
    public void quiz_answer_isIncorrect() {
        assertThat(getQuiz().isAnswerCorrect(null), is(false));
    }

    @Test
    public void quiz_equalsSelf_true() {
        assertThat(getQuiz().equals(getQuiz()), is(true));
    }

    @Test
    public void quiz_equalsOther_false() {
        assertThat(getQuiz().equals("Foobar"), is(false));
    }

    @Test
    public void quiz_hashCode_consistent() {
        assertThat(getQuiz().hashCode() == getQuiz().hashCode(), is(true));
    }

    /**
     * Implementations need to check that the expected answer is equal
     * to the one returned from the quiz.
     */
    @Test
    public abstract void quiz_answer_correctlyStored();

    /**
     * Implementations need to return a newly instantiated {@link Quiz} here.
     *
     * @return The instantiated {@link Quiz}.
     */
    public abstract Q getQuiz();

    /**
     * Implementations have to return the expected quiz type for the corresponding {@link Quiz}.
     * This should <b>NOT</b> be obtained by calling getQuiz.getType()!
     *
     * @return The expected {@link QuizType}.
     */
    public abstract QuizType getExpectedQuizType();

}
