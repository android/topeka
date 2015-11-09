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

package com.google.samples.apps.topeka.widget;

import com.google.samples.apps.topeka.helper.ViewUtils;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.SharedElementCallback;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * This callback allows a shared TextView to resize text and start padding during transition.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TextSharedElementCallback extends SharedElementCallback {

    private final int mInitialPaddingStart;
    private final float mInitialTextSize;
    private float mTargetViewTextSize;
    private int mTargetViewPaddingStart;
    private static final String TAG = "TextResize";

    public TextSharedElementCallback(float initialTextSize, int initialPaddingStart) {
        mInitialTextSize = initialTextSize;
        mInitialPaddingStart = initialPaddingStart;
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements,
                                     List<View> sharedElementSnapshots) {
        TextView targetView = getTextView(sharedElements);
        if (targetView == null) {
            Log.w(TAG, "onSharedElementStart: No shared TextView, skipping.");
            return;
        }
            mTargetViewTextSize = targetView.getTextSize();
            mTargetViewPaddingStart = targetView.getPaddingStart();
            // Setup the TextView's start values.
            targetView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mInitialTextSize);
            ViewUtils.setPaddingStart(targetView, mInitialPaddingStart);
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements,
                                   List<View> sharedElementSnapshots) {
        TextView initialView = getTextView(sharedElements);

        if (initialView == null) {
            Log.w(TAG, "onSharedElementEnd: No shared TextView, skipping");
            return;
        }

        // Setup the TextView's end values.
        initialView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTargetViewTextSize);
        ViewUtils.setPaddingStart(initialView, mTargetViewPaddingStart);

        // Re-measure the TextView (since the text size has changed).
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        initialView.measure(widthSpec, heightSpec);
        initialView.requestLayout();
    }

    @Nullable
    private TextView getTextView(List<View> sharedElements) {
        TextView targetView = null;
        for (int i = 0; i < sharedElements.size(); i++) {
            if (sharedElements.get(i) instanceof TextView) {
                targetView = (TextView) sharedElements.get(i);
            }
        }
        return targetView;
    }

}
