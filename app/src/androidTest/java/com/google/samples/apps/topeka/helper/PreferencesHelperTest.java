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
package com.google.samples.apps.topeka.helper;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Player;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class PreferencesHelperTest {

    private static final Player TEST_PLAYER = new Player("Zaphod", "B", Avatar.FOUR);

    @Test
    public void performPreferenceCycle() throws Exception {
        final Context context = InstrumentationRegistry.getTargetContext();
        PreferencesHelper.signOut(context);
        assertThat(PreferencesHelper.isSignedIn(context), is(false));
        PreferencesHelper.writeToPreferences(context, TEST_PLAYER);
        final Player resultingPlayer = PreferencesHelper.getPlayer(context);
        assertThat(resultingPlayer, is(TEST_PLAYER));
        assertThat(PreferencesHelper.isSignedIn(context), is(true));
    }
}