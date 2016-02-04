/*
 * Copyright 2015 Google Inc.
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
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Removes redundancy during test creation for Quiz tests.
 *
 * @param <Q> The Quiz type under test.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public abstract class AbsQuizAndroidTestCase<Q extends Quiz> {

    protected static final String QUESTION = "Is this the real world? Is this just fantasy?";
    protected static final int[] INT_ARRAY = new int[]{0, 1, 2};
    protected static final String[] STRING_ARRAY = new String[]{"one", "two", "three", "four"};

    private Q quizUnderTest;

    @Before
    public void setUp() throws Exception {
        quizUnderTest = getQuiz();
    }

    @Test
    public void writeToParcel() {
        Parcel dest = Parcel.obtain();
        quizUnderTest.writeToParcel(dest, 0);
        dest.setDataPosition(0);
        Quiz unparcelled = Quiz.CREATOR.createFromParcel(dest);
        assertThat(quizUnderTest, is(unparcelled));
    }

    /**
     * Implementations need to return a newly instantiated {@link Quiz} here.
     *
     * @return The newly instantiated {@link Quiz}.
     */
    public abstract Q getQuiz();
}
