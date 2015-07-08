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
package com.google.samples.apps.topeka.widget.fab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.widget.outlineprovider.RoundOutlineProvider;

/**
 * Generic implementation of the
 * <a href=
 * "http://www.google.com/design/spec/components/buttons.html#buttons-floating-action-button">
 * floating action button</a>
 * described in the material design guidelines.
 */
public class FloatingActionButton extends ImageView {

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setClickable(true);
        setClipToOutline(true);
        setScaleType(ScaleType.CENTER_INSIDE);
        setBackgroundResource(R.drawable.fab_background);
        setElevation(getResources().getDimension(R.dimen.elevation_fab));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            setOutlineProvider(new RoundOutlineProvider(Math.min(w, h)));
        }
    }
}
