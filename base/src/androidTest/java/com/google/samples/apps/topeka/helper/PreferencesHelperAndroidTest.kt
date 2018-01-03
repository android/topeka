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

package com.google.samples.apps.topeka.helper

import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.google.samples.apps.topeka.model.Player
import com.google.samples.apps.topeka.model.TEST_AVATAR
import com.google.samples.apps.topeka.model.TEST_FIRST_NAME
import com.google.samples.apps.topeka.model.TEST_LAST_INITIAL
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Tests whether the perference helper works correctly.
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
class PreferencesHelperAndroidTest {

    private val TEST_PLAYER = Player(TEST_FIRST_NAME, TEST_LAST_INITIAL, TEST_AVATAR)

    @Before fun clearPreferences() = InstrumentationRegistry.getTargetContext().logout()

    /**
     * Creates a player and stores it to the preferences. Then tries to read it.
     */
    @Test fun performPreferenceCycle() {
        with(InstrumentationRegistry.getTargetContext()) {
            logout()
            assertThat(isLoggedIn(), `is`(false))
            storePlayerLocally(TEST_PLAYER)
            val resultingPlayer = getPlayer()
            assertThat<Player>(resultingPlayer, `is`(TEST_PLAYER))
            assertThat(isLoggedIn(), `is`(true))
        }
    }

}