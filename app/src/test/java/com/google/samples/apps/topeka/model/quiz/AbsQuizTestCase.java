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

import android.os.Parcel;

import junit.framework.TestCase;

/**
 * Removes redundancy during test creation for Quiz tests.
 *
 * @param <Q> The Quiz type under test.
 */
public abstract class AbsQuizTestCase<Q extends Quiz> extends TestCase {


    protected static final String QUESTION = "Is this the real world? Is this just fantasy?";
    protected static final int[] INT_ARRAY = new int[]{0, 1, 2};
    protected static final String[] STRING_ARRAY = new String[]{"one", "two", "three", "four"};

    private Q quizUnderTest;

    @Override
    protected void setUp() throws Exception {
        quizUnderTest = getQuiz();
        super.setUp();
    }

    public void testGetType() throws Exception {
        assertEquals(getExpectedQuizType(), getQuiz().getType());
    }

    public void testGetQuestion() throws Exception {
        assertEquals(QUESTION, getQuiz().getQuestion());
    }

    public void testWriteToParcel() {
        Parcel dest = Parcel.obtain();
        quizUnderTest.writeToParcel(dest, 0);
        dest.setDataPosition(0);
        Quiz unparcelled = Quiz.CREATOR.createFromParcel(dest);
        assertEquals(quizUnderTest, unparcelled);
    }

    /**
     * Implementations need to check that the expected answer is equal
     * to the one returned from the quiz.
     *
     */
    public abstract void testGetAnswer();

    /**
     * Implementations need to return a newly instantiated {@link Quiz} here.
     *
     * @return The newly instantiated {@link Quiz}.
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
