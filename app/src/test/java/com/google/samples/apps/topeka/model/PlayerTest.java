/*
 * Copyright 2015 Google Inc. All Rights Reserved.
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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class PlayerTest {

    public static final String FIRST_NAME = "Zaphod";
    public static final Avatar AVATAR = Avatar.FOUR;
    private final String LAST_INITIAL = "B";

    @Test
    public void construct_fullInfo() {
        assertThat(new Player(FIRST_NAME, LAST_INITIAL, AVATAR), notNullValue());
    }

       @Test
    public void equals_self_true() throws Exception {
        Player player = getPlayer();
        assertThat(player.equals(player), is(true));
    }

    @Test
    public void equals_similar_true() throws Exception {
        assertThat(getPlayer().equals(getPlayer()), is(true));
    }

    @Test
    public void equals_other_false() throws Exception {
        Player player = getPlayer();
        Player other = new Player(LAST_INITIAL, FIRST_NAME, Avatar.FOUR);
        assertThat(player.equals(other), is(false));
    }

    @Test
    public void hashCode_isConsistent() throws Exception {
        Player player = getPlayer();
        assertThat(player.hashCode() == player.hashCode(), is(true));
    }

    private Player getPlayer() {
        return new Player(FIRST_NAME, LAST_INITIAL, AVATAR);
    }
}