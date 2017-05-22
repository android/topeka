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

import android.os.Parcel
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests whether the parcelable helper stores and retrieves data correctly.
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
class ParcelableHelperAndroidTest {

    @Test
    fun writeReadBoolean() {
        val testParcel = Parcel.obtain()
        val testValue = true
        testParcel.writeBoolean(testValue)
        testParcel.setDataPosition(0)
        val resultValue = testParcel.readBoolean()
        assertThat(testValue, `is`(resultValue))
        testParcel.recycle()
    }
}