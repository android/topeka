/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.topeka.activity.quiz;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.QuizActivity;
import com.google.samples.apps.topeka.helper.PreferencesHelper;
import com.google.samples.apps.topeka.helper.SolveQuizHelper;
import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class BaseQuizActivityTest {

    private List<Category> mCategories;
    @Rule
    public ActivityTestRule<QuizActivity> mActivityRule =
            new ActivityTestRule<QuizActivity>(QuizActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    Context targetContext = InstrumentationRegistry.getTargetContext();
                    PreferencesHelper.signOut(targetContext);
                    TopekaDatabaseHelper.reset(targetContext);
                    PreferencesHelper.writeToPreferences(targetContext,
                            new Player("Zaphod", "B", Avatar.FIVE));
                }

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getTargetContext();
                    mCategories = TopekaDatabaseHelper.getCategories(targetContext, false);
                    return QuizActivity.getStartIntent(targetContext,
                            getCurrentCategory());
                }
            };

    abstract int getCategory();

    @Before
    public void registerIdlingResources() {
        Espresso.registerIdlingResources(mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void categoryName_isDisplayed() {
        onView(withText(getCurrentCategory().getName())).check(matches(isDisplayed()));
    }

    @Test
    public void category_solveCorrectly() {
        testCategory();
    }

    protected void testCategory() {
        final Category category = getCurrentCategory();
        onView(withId(R.id.fab_quiz)).perform(click());
        for (Quiz quiz : category.getQuizzes()) {
            SolveQuizHelper.solveQuiz(quiz);
            onView(allOf(withId(R.id.submitAnswer), isDisplayed()))
                    .check(matches(isDisplayed()))
                    .perform(click());
        }
    }

    private Category getCurrentCategory() {
        return mCategories.get(getCategory());
    }

    @After
    public void unregisterIdlingResources() {
        Espresso.unregisterIdlingResources(mActivityRule.getActivity().getCountingIdlingResource());
    }

}
