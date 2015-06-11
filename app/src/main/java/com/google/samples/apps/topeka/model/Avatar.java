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

import android.support.annotation.DrawableRes;

import com.google.samples.apps.topeka.R;

/**
 * The available avatars with their corresponding drawable resource ids.
 */
public enum Avatar {

    ONE(R.drawable.avatar_1),
    TWO(R.drawable.avatar_2),
    THREE(R.drawable.avatar_3),
    FOUR(R.drawable.avatar_4),
    FIVE(R.drawable.avatar_5),
    SIX(R.drawable.avatar_6),
    SEVEN(R.drawable.avatar_7),
    EIGHT(R.drawable.avatar_8),
    NINE(R.drawable.avatar_9),
    TEN(R.drawable.avatar_10),
    ELEVEN(R.drawable.avatar_11),
    TWELVE(R.drawable.avatar_12),
    THIRTEEN(R.drawable.avatar_13),
    FOURTEEN(R.drawable.avatar_14),
    FIFTEEN(R.drawable.avatar_15),
    SIXTEEN(R.drawable.avatar_16);

    private static final String TAG = "Avatar";

    private final int mResId;

    Avatar(@DrawableRes final int resId) {
        mResId = resId;
    }

    @DrawableRes
    public int getDrawableId() {
        return mResId;
    }

    public String getNameForAccessibility() {
        return TAG + " " + ordinal() + 1;
    }
}
