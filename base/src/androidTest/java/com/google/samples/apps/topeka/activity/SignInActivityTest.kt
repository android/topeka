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

package com.google.samples.apps.topeka.activity

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isChecked
import android.support.test.espresso.matcher.ViewMatchers.isClickable
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.isFocusable
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.google.samples.apps.topeka.TestLogin
import com.google.samples.apps.topeka.base.R
import com.google.samples.apps.topeka.helper.ActivityLaunchHelper
import com.google.samples.apps.topeka.helper.login
import com.google.samples.apps.topeka.helper.logout
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.model.TEST_AVATAR
import com.google.samples.apps.topeka.model.TEST_FIRST_NAME
import com.google.samples.apps.topeka.model.TEST_LAST_INITIAL
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.isEmptyOrNullString
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SignInActivityTest {

    @Suppress("unused") // actually used by Espresso
    val rule
        @Rule get() = object :
                ActivityTestRule<SignInActivity>(SignInActivity::class.java) {
            override fun beforeActivityLaunched() {
                InstrumentationRegistry.getTargetContext().logout()
                login = TestLogin
            }

            override fun getActivityIntent() = ActivityLaunchHelper.signInIntent(edit = true)
        }

    @Before fun clearPreferences() {
        InstrumentationRegistry.getTargetContext().logout()
    }

    @Test fun checkFab_initiallyNotDisplayed() {
        onView(withId(R.id.done)).check(matches(not(isDisplayed())))
    }

    @Test fun signIn_withoutFirstNameFailed() {
        inputData(null, TEST_LAST_INITIAL, TEST_AVATAR)
        onDoneView().check(matches(not(isDisplayed())))
    }

    @Test fun signIn_withoutLastInitialFailed() {
        inputData(TEST_FIRST_NAME, null, TEST_AVATAR)
        onDoneView().check(matches(not(isDisplayed())))
    }

    @Test fun signIn_withoutAvatarFailed() {
        inputData(TEST_FIRST_NAME, TEST_LAST_INITIAL, null)
        onDoneView().check(matches(not(isDisplayed())))
    }

    @Test fun signIn_withAllPlayerPreferencesSuccessfully() {
        inputData(TEST_FIRST_NAME, TEST_LAST_INITIAL, TEST_AVATAR)
        onDoneView().check(matches(isDisplayed()))
    }

    /* TODO Debug: Espresso does currently not continue after this test. Commenting to keep pace.
    @Test fun signIn_performSignIn() {
        inputData(TEST_FIRST_NAME, TEST_LAST_INITIAL, TEST_AVATAR)
        onDoneView().perform(click())

        assertThat(InstrumentationRegistry.getTargetContext().isLoggedIn(), `is`(true))
    }
    */

    private fun onDoneView() = onView(withId(R.id.done))

    @Test fun signIn_withLongLastName() {
        typeAndHideKeyboard(R.id.last_initial, TEST_FIRST_NAME)
        val expectedValue = TEST_FIRST_NAME[0].toString()
        onView(withId(R.id.last_initial)).check(matches(withText(expectedValue)))
    }

    private fun inputData(firstName: String?, lastInitial: String?, avatar: Avatar?) {
        if (firstName != null) typeAndHideKeyboard(R.id.first_name, firstName)
        if (lastInitial != null) typeAndHideKeyboard(R.id.last_initial, lastInitial)
        if (avatar != null) clickAvatar(avatar)
    }

    private fun typeAndHideKeyboard(targetViewId: Int, text: String) {
        onView(withId(targetViewId)).perform(typeText(text), closeSoftKeyboard())
    }

    private fun clickAvatar(avatar: Avatar) {
        onData(equalTo(avatar))
                .inAdapterView(withId(R.id.avatars))
                .perform(click())
    }

    @Test fun firstName_isInitiallyEmpty() = editTextIsEmpty(R.id.first_name)

    @Test fun lastInitial_isInitiallyEmpty() = editTextIsEmpty(R.id.last_initial)

    private fun editTextIsEmpty(id: Int) {
        onView(withId(id)).check(matches(withText(isEmptyOrNullString())))
    }

    @Test fun avatar_allDisplayed() = checkOnAvatar(isDisplayed())

    @Test fun avatar_isEnabled() = checkOnAvatar(isEnabled())

    @Test fun avatar_notFocusable() = checkOnAvatar(not(isFocusable()))

    @Test fun avatar_notClickable() = checkOnAvatar(not(isClickable()))

    @Test fun avatar_noneChecked() = checkOnAvatar(not(isChecked()))

    private fun checkOnAvatar(matcher: Matcher<View>) {
        (0 until Avatar.values().size).forEach {
            onData(equalTo(Avatar.values()[it]))
                    .inAdapterView(withId(R.id.avatars))
                    .check(matches(matcher))
        }
    }
}