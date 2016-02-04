/*
 * Copyright 2015 Google Inc.
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

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class PlayerAndroidTest {

    private static final Avatar AVATAR = Avatar.TWELVE;
    private static final String LAST_INITIAL = "a";
    private static final String FIRST_NAME = "first";

    private static Player getPlayerUnderTest() {
        return new Player(FIRST_NAME, LAST_INITIAL, AVATAR);
    }

    @Test
    public void writeToParcel() throws Exception {
        Player initial = getPlayerUnderTest();
        Parcel dest = Parcel.obtain();
        initial.writeToParcel(dest, 0);
        dest.setDataPosition(0);
        Player unparcelled = new Player(dest);
        assertThat(initial, is(unparcelled));
    }
}