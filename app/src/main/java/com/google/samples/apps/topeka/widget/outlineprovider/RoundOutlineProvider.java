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

package com.google.samples.apps.topeka.widget.outlineprovider;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Creates round outlines for views.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundOutlineProvider extends ViewOutlineProvider {

    private final int mSize;

    public RoundOutlineProvider(int size) {
        if (0 > size) {
            throw new IllegalArgumentException("size needs to be > 0. Actually was " + size);
        }
        mSize = size;
    }

    @Override
    public final void getOutline(View view, Outline outline) {
        outline.setOval(0, 0, mSize, mSize);
    }

}
