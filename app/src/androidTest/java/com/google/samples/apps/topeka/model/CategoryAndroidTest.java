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

package com.google.samples.apps.topeka.model;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class CategoryAndroidTest {

    private static final String CATEGORY_NAME = "test";
    private static final String CATEGORY_ID = "testId";
    private static final Theme CATEGORY_THEME = Theme.topeka;

    private static Category getCategoryUnderTest() {
        return new Category(CATEGORY_NAME, CATEGORY_ID, CATEGORY_THEME, getQuizzes(), false);
    }

    private static List<Quiz> getQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        quizzes.add(new TrueFalseQuiz("huh?", true, false));
        quizzes.add(new FillBlankQuiz("so?", "yeah", "go", "stop", false));
        return quizzes;
    }

    @Test
    public void writeToParcel() {
        Category initialCategory = getCategoryUnderTest();
        Parcel dest = Parcel.obtain();
        initialCategory.writeToParcel(dest, 0);
        dest.setDataPosition(0);
        Category unparcelled = new Category(dest);
        assertThat(initialCategory, is(unparcelled));
    }
}