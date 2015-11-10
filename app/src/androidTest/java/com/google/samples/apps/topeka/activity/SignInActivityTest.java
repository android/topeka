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
import com.google.samples.apps.topeka.model.Avatar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;

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
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.ONE)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_02() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.TWO)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_03() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.THREE)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_04() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.FOUR)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_05() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.FIVE)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_06() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.SIX)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_07() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.SEVEN)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_08() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.EIGHT)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_09() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.NINE)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_10() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.TEN)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_11() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.ELEVEN)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_12() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.TWELVE)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_13() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.THIRTEEN)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_14() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.FOURTEEN)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_15() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.FIFTEEN)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void chooseAvatar_16() {
        onData(allOf(is(instanceOf(Avatar.class)), is(Avatar.SIXTEEN)))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));
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
