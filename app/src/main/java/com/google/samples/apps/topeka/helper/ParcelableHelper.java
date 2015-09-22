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

import android.os.Parcel;

/**
 * Collection of shared methods ease parcellation of special types.
 */
public class ParcelableHelper {

    private ParcelableHelper() {
        //no instance
    }

    /**
     * Writes a single boolean to a {@link android.os.Parcel}.
     *
     * @param dest Destination of the value.
     * @param toWrite Value to write.
     * @see ParcelableHelper#readBoolean(android.os.Parcel)
     */
    public static void writeBoolean(Parcel dest, boolean toWrite) {
        dest.writeInt(toWrite ? 0 : 1);
    }

    /**
     * Retrieves a single boolean from a Parcel.
     *
     * @param in The source containing the stored boolean.
     * @see ParcelableHelper#writeBoolean(android.os.Parcel, boolean)
     */
    public static boolean readBoolean(Parcel in) {
        return 0 == in.readInt();
    }

    /**
     * Allows memory efficient parcelation of enums.
     *
     * @param dest Destination of the value.
     * @param e Value to write.
     */
    public static void writeEnumValue(Parcel dest, Enum e) {
        dest.writeInt(e.ordinal());
    }
}
