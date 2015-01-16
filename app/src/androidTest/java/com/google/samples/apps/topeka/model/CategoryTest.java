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
package com.google.samples.apps.topeka.model;

import android.os.Parcel;
import android.test.suitebuilder.annotation.MediumTest;

import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

@MediumTest
public class CategoryTest extends TestCase {

    private static final String CATEGORY_NAME = "test";
    private static final String CATEGORY_ID = "testId";
    private static final Theme CATEGORY_THEME = Theme.topeka;

    public void testWriteToParcel() {
        Category initialCategory = getCategoryUnderTest();
        Parcel dest = Parcel.obtain();
        initialCategory.writeToParcel(dest, 0);
        dest.setDataPosition(0);
        Category unparcelled = new Category(dest);
        assertEquals(initialCategory, unparcelled);
    }

    private static Category getCategoryUnderTest() {
        return new Category(CATEGORY_NAME, CATEGORY_ID, CATEGORY_THEME, getQuizzes(), false);
    }

    private static List<Quiz> getQuizzes() {
        List<Quiz> quizzes = new ArrayList<Quiz>();
        quizzes.add(new TrueFalseQuiz("huh?", true, false));
        quizzes.add(new FillBlankQuiz("so?", "yeah", "go", "stop", false));
        return quizzes;
    }
}