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

package com.google.samples.apps.topeka.widget

import android.annotation.TargetApi
import android.os.Build
import android.support.v4.app.SharedElementCallback
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.samples.apps.topeka.helper.ViewUtils

/**
 * This callback allows a shared TextView to resize text and start padding during transition.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
open class TextSharedElementCallback(
        private val initialTextSize: Float,
        private val initialPaddingStart: Int
) : SharedElementCallback() {

    private var targetViewTextSize = 0f
    private var targetViewPaddingStart = 0

    override fun onSharedElementStart(sharedElementNames: List<String>?,
                                      sharedElements: List<View>,
                                      sharedElementSnapshots: List<View>?) {
        val targetView = getTextView(sharedElements)
        if (targetView == null) {
            Log.w(TAG, "onSharedElementStart: No shared TextView, skipping.")
            return
        }
        targetViewTextSize = targetView.textSize
        targetViewPaddingStart = targetView.paddingStart
        // Setup the TextView's start values.
        targetView.setTextSize(TypedValue.COMPLEX_UNIT_PX, initialTextSize)
        ViewUtils.setPaddingStart(targetView, initialPaddingStart)
    }

    override fun onSharedElementEnd(sharedElementNames: List<String>?,
                                    sharedElements: List<View>,
                                    sharedElementSnapshots: List<View>?) {
        val initialView = getTextView(sharedElements)

        if (initialView == null) {
            Log.w(TAG, "onSharedElementEnd: No shared TextView, skipping")
            return
        }

        // Setup the TextView's end values.
        initialView.setTextSize(TypedValue.COMPLEX_UNIT_PX, targetViewTextSize)
        ViewUtils.setPaddingStart(initialView, targetViewPaddingStart)

        // Re-measure the TextView (since the text size has changed).
        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        with(initialView) {
            measure(widthSpec, heightSpec)
            requestLayout()
        }
    }

    private fun getTextView(sharedElements: List<View>) =
            sharedElements.first { it is TextView } as TextView?

    companion object {
        private const val TAG = "TextResize"
    }

}
