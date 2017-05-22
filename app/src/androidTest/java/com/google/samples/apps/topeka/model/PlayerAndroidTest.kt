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

package com.google.samples.apps.topeka.model

import android.os.Parcel
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

val TEST_FIRST_NAME = "Zaphod"
val TEST_LAST_INITIAL = "B"
val TEST_AVATAR = Avatar.TWELVE

val TEST_PLAYER = Player(TEST_FIRST_NAME, TEST_LAST_INITIAL, TEST_AVATAR)

@SmallTest
@RunWith(AndroidJUnit4::class)
class PlayerAndroidTest {

    @Test fun writeToParcel() {
        val dest = Parcel.obtain()
        TEST_PLAYER.writeToParcel(dest, 0)
        dest.setDataPosition(0)
        val unparcelled = Player.CREATOR.createFromParcel(dest)
        assertThat(TEST_PLAYER, `is`(unparcelled))
    }

}
