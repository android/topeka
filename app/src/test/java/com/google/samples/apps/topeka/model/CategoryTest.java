/*
 * Copyright 2015 Google Inc. All Rights Reserved.
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

import com.google.samples.apps.topeka.model.quiz.Quiz;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CategoryTest {

    private static final String NAME = "Foo";
    private static final String ID = "id";
    private static final Theme THEME = Theme.blue;
    private static final ArrayList<Quiz> QUIZZES = new ArrayList<>();

    @Test
    public void construct_withoutScores() {
        assertThat(new Category(NAME, ID, THEME, new ArrayList<Quiz>(), false), notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void construct_withoutScores_fails() {
        new Category(NAME, ID, THEME, null, false);
    }

    @Test
    public void construct_withScores_sameSizeQuizAndScores() {
        assertThat(new Category(NAME, ID, THEME, new ArrayList<Quiz>(), new int[0], false),
                notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_withScores_failsDifferentSizeQuizAndScores() {
        new Category(NAME, ID, THEME, new ArrayList<Quiz>(), new int[2], false);
    }

    @Test(expected = NullPointerException.class)
    public void construct_withScores_noQuizzes_fails() {
        new Category(NAME, ID, THEME, null, new int[0], false);
    }

    @Test
    public void equals_self_true() {
        Category category = createCategory();
        assertThat(category.equals(category), is(true));
    }

    @Test
    public void equals_similar_true() {
        Category category = createCategory();
        assertThat(category.equals(createCategory()), is(true));
    }

    @Test
    public void equals_other_false() {
        Category otherCategory = new Category("Foo", "Bar", Theme.topeka, QUIZZES, true);
        assertThat(createCategory().equals(otherCategory), is(false));
    }

    @Test
    public void hashCode_isConsistent() {
        Category category = createCategory();
        assertThat(category.hashCode() == category.hashCode(), is(true));
    }

    @Test
    public void hashCode_failsForDifferent() {
        Category category = createCategory();
        Category other = new Category(ID, NAME, THEME, QUIZZES, true);
        assertThat(category.hashCode() == other.hashCode(), is(false));
    }

    private Category createCategory() {
        return new Category(NAME, ID, THEME, QUIZZES, false);
    }

}