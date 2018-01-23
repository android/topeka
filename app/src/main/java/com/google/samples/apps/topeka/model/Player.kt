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
import com.google.android.gms.auth.api.credentials.Credential
import com.google.samples.apps.topeka.helper.TAG

/**
 * Stores values to identify the subject that is currently attempting to solve quizzes.
 */
data class Player(
        var firstName: String?,
        var lastInitial: String?,
        var avatar: Avatar?,
        /**
         * Only used for SmartLock purposes.
         */
        val email: String? = TAG
) : Parcelable {

    /**
     * Create a Player from SmartLock API.
     */
    constructor(credential: Credential) :
            // The avatar choice could be done by fetching data stored on a server, but this
            // is a sample after all. So we don't bother with creating a server and storing
            // this information at the moment.
            this(firstName = credential.name?.substringBefore(" "),
                    lastInitial = credential.name?.substringAfterLast(" ")?.get(0).toString(),
                    avatar = Avatar.ELEVEN,
                    email = credential.id ?: TAG)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(firstName)
            writeString(lastInitial)
            writeString(email)
            avatar?.run { writeInt(ordinal) }
        }
    }

    override fun toString() = "$firstName $lastInitial"

    fun valid() = !(firstName.isNullOrEmpty() || lastInitial.isNullOrEmpty()) && avatar != null

    override fun describeContents() = 0

    /**
     * Create a [Credential] containing information of this [Player].
     */
    fun toCredential(): Credential = Credential.Builder(email)
            .setName(toString())
            .build()

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<Player> = object : Parcelable.Creator<Player> {

            override fun createFromParcel(parcel: Parcel) = with(parcel) {
                Player(firstName = readString(),
                        lastInitial = readString(),
                        email = readString(),
                        avatar = Avatar.values()[readInt()])
            }

            override fun newArray(size: Int): Array<Player?> = arrayOfNulls(size)
        }
    }
}
