/*
 * Copyright 2017 Google Inc.
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

package com.google.samples.apps.topeka.helper

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.model.Player
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper

const val PLAYER_PREFERENCES = "playerPreferences"
const val PREFERENCE_FIRST_NAME = "$PLAYER_PREFERENCES.firstName"
const val PREFERENCE_LAST_INITIAL = "$PLAYER_PREFERENCES.lastInitial"
const val PREFERENCE_AVATAR = "$PLAYER_PREFERENCES.avatar"

@SuppressLint("CommitPrefEdits")
private fun Context.editPlayer() = getPlayerPreferences().edit()

private fun Context.getPlayerPreferences() =
        getSharedPreferences(PLAYER_PREFERENCES, Context.MODE_PRIVATE)


fun Context.signOut() {
    editPlayer().clear().commit()
    database().reset()
}

fun Context.isSignedIn(): Boolean = with(getPlayerPreferences()) {
    contains(PREFERENCE_FIRST_NAME)
            && contains(PREFERENCE_LAST_INITIAL)
            && contains(PREFERENCE_AVATAR)
}

fun Context.getPlayer() = with(getPlayerPreferences()) {
    Player(getString(PREFERENCE_FIRST_NAME, null),
            getString(PREFERENCE_LAST_INITIAL, null),
            getString(PREFERENCE_AVATAR, null)
                    ?.let { Avatar.valueOf(it) })
}

fun Context.savePlayer(player: Player) {
    with(player) {
        if (valid())
            editPlayer()
                    .putString(PREFERENCE_FIRST_NAME, firstName)
                    .putString(PREFERENCE_LAST_INITIAL, lastInitial)
                    .putString(PREFERENCE_AVATAR, avatar?.name)
                    .apply()
    }
}

fun Context.database() = TopekaDatabaseHelper.getInstance(this)

fun Context.inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean): View =
        LayoutInflater.from(this).inflate(resource, root, attachToRoot)