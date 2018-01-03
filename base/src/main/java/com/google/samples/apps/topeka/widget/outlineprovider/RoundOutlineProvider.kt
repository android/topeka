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

package com.google.samples.apps.topeka.widget.outlineprovider

import android.annotation.TargetApi
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider

/**
 * Creates round outlines for views.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class RoundOutlineProvider(private val size: Int) : ViewOutlineProvider() {

    init {
        if (0 > size) throw IllegalArgumentException("size needs to be > 0. Actually was $size")
    }

    override fun getOutline(view: View, outline: Outline) = outline.setOval(0, 0, size, size)

}
