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

package com.google.samples.apps.topeka.activity.quiz

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.google.samples.apps.topeka.SolveQuizUtil
import com.google.samples.apps.topeka.TestLogin
import com.google.samples.apps.topeka.activity.QuizActivity
import com.google.samples.apps.topeka.activity.countingIdlingResource
import com.google.samples.apps.topeka.helper.ActivityLaunchHelper
import com.google.samples.apps.topeka.helper.database
import com.google.samples.apps.topeka.helper.login
import com.google.samples.apps.topeka.helper.logout
import com.google.samples.apps.topeka.helper.storePlayerLocally
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.Player
import com.google.samples.apps.topeka.quiz.R
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract
/**
 * Parent class for all quiz activity tests.
 */
class BaseQuizActivityTest {

    @Suppress("unused") // actually used by Espresso
    val activityRule @Rule get() = object :
            ActivityTestRule<QuizActivity>(QuizActivity::class.java) {
        override fun beforeActivityLaunched() {
            login = TestLogin
            with(InstrumentationRegistry.getTargetContext()) {
                logout()
                // Circumventing SmartLock loginPlayer at this point.
                storePlayerLocally(Player("Zaphod", "B", Avatar.FIVE))
            }
        }

        override fun getActivityIntent() = ActivityLaunchHelper.quizIntent(currentCategory)
    }

    private val categories: List<Category> get() {
        return InstrumentationRegistry.getTargetContext().database().getCategories(true)
    }

    /**
     * @return The category's position.
     */
    internal abstract val category: Int

    private val currentCategory: Category get() = categories[category]

    /**
     * Register idling resources for the activity under test.
     */
    @Before fun registerIdlingResources() {
        Espresso.registerIdlingResources(countingIdlingResource)
    }

    /**
     * Unregister idling resources for the activity under test.
     */
    @After fun unregisterIdlingResources() {
        Espresso.unregisterIdlingResources(countingIdlingResource)
    }

    /**
     * Tests whether a category with it's given name is currently didsplayed.
     */
    @Test fun categoryName_isDisplayed() {
        onView(withText(currentCategory.name)).check(matches(isDisplayed()))
    }

    /**
     * Presses back from within the toolbar.
     */
    @Test fun goBack_fromToolbar() {
        onView(withId(R.id.back)).perform(click())
    }

    @Test fun category_solve() {
        testCategory()
    }

    /**
     * End to end test for the given category.
     */
    protected fun testCategory() {
        val (_, _, _, quizzes) = currentCategory
        onView(withId(R.id.fab_quiz)).perform(click())
        quizzes.forEach {
            SolveQuizUtil.solveQuiz(it)
            onView(allOf<View>(withId(R.id.submitAnswer), isDisplayed()))
                    .check(matches(isDisplayed()))
                    .perform(click())
        }
    }

}

val FOOD = 0
val GENERAL_KNOWLEDGE = 1
val HISTORY = 2
val GEOGRAPHY = 3
val SCIENCE_AND_NATURE = 4
val TV_AND_MOVIES = 5
val MUSIC = 6
val ENTERTAINMENT = 7
val SPORTS = 8
