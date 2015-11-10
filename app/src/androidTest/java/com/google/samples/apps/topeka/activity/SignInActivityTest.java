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

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.PreferencesHelper;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInActivityTest {

    private static final String TEST_FIRST_NAME = "Zaphod";
    private static final String TEST_LAST_INITIAL = "B";

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule =
            new ActivityTestRule<SignInActivity>(SignInActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    PreferencesHelper.signOut(InstrumentationRegistry.getTargetContext());
                }
            };

    @Before
    public void clearPreferences() throws Exception {
        PreferencesHelper.signOut(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void checkFab_initiallyNotDisplayed() {
        onView(withId(R.id.done)).check(matches(not(isDisplayed())));
    }

    @Test
    public void checkIfFirstNameEditTextIsInitiallyEmpty() {
        onView(withId(R.id.first_name)).check(matches(withText(isEmptyOrNullString())));
    }

    @Test
    public void checkIfLastInitialEditTextIsInitiallyEmpty() {
        onView(withId(R.id.last_initial)).check(matches(withText(isEmptyOrNullString())));
    }

    @Test
    public void signIn_performSuccessful() {
        inputData();
        onView(withId(R.id.done))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
    }

    @Test
    public void chooseAvatar_01() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 01")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_02() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 11")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_03() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 21")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_04() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 31")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_05() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 41")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_06() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 51")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_07() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 61")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_08() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 71")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_09() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 81")))
                .check(matches(isDisplayed()))
                .perform(click()).check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_10() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 91")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_11() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 101")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_12() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 111")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_13() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 121")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_14() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 131")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_15() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 141")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_16() {
        onView(allOf(withParent(withId(R.id.avatars)), withContentDescription("Avatar 151")))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(Matchers.allOf(isChecked())));
    }

    @Test
    public void signIn_withLongLastName() {
        inputData();
        onView(withId(R.id.last_initial)).perform(typeText(TEST_FIRST_NAME), closeSoftKeyboard());
        onView(withId(R.id.last_initial)).check(matches(withText(TEST_LAST_INITIAL)));
    }

    private void inputData() {
        onView(withId(R.id.first_name)).perform(typeText(TEST_FIRST_NAME), closeSoftKeyboard());
        onView(withId(R.id.last_initial)).perform(typeText(TEST_LAST_INITIAL), closeSoftKeyboard());
    }
}
