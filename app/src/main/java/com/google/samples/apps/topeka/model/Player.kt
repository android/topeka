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
import android.os.Parcelable

/**
 * Stores values to identify the subject that is currently attempting to solve quizzes.
 */
data class Player(
        val firstName: String?,
        val lastInitial: String?,
        val avatar: Avatar?
) : Parcelable {

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(firstName)
            writeString(lastInitial)
            if (avatar != null) writeInt(avatar.ordinal)
        }
    }

    fun valid() = !(firstName.isNullOrEmpty() || lastInitial.isNullOrEmpty()) && avatar != null

    override fun describeContents() = 0

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<Player> = object : Parcelable.Creator<Player> {

            override fun createFromParcel(parcel: Parcel) = with(parcel) {
                Player(firstName = readString(),
                        lastInitial = readString(),
                        avatar = Avatar.values()[readInt()])
            }

            override fun newArray(size: Int): Array<Player?> = arrayOfNulls(size)
        }
    }
}
