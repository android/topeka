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

package com.google.samples.apps.topeka.activity;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.PreferencesHelper;
import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategorySelectionActivityTest {

    private Context mTargetContext;
    private List<Category> mCategories;

    @Rule
    public ActivityTestRule<CategorySelectionActivity> mActivityRule =
            new ActivityTestRule<CategorySelectionActivity>(CategorySelectionActivity.class) {

                @Override
                protected void beforeActivityLaunched() {
                    PreferencesHelper.signOut(InstrumentationRegistry.getTargetContext());
                }

                @Override
                protected Intent getActivityIntent() {
                    mTargetContext = InstrumentationRegistry.getTargetContext();
                    final Player player = new Player("Zaphod", "B", Avatar.EIGHT);
                    return CategorySelectionActivity.getStartIntent(mTargetContext, player);
                }
            };

    @Before
    public void loadCategories() {
        mCategories = TopekaDatabaseHelper.getCategories(mTargetContext, false);
    }

    @Test
    public void allCategories_areDisplayed() throws InterruptedException {
        String categoryName;
        for (int i = 0; i < mCategories.size(); i++) {
            categoryName = mCategories.get(i).getName();
            onView(withId(R.id.categories))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, scrollTo()));
            onView(withText(categoryName)).check(matches(isDisplayed()));

        }
    }

    @Test
    public void signOut() {
        openActionBarOverflowOrOptionsMenu(mTargetContext);
        onView(withText(R.string.sign_out)).perform(click());
        assertFalse(PreferencesHelper.isSignedIn(mTargetContext));
    }
}
