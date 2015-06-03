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

import android.support.annotation.ColorRes;
import android.support.annotation.StyleRes;

import com.google.samples.apps.topeka.R;

/**
 * A way to make simple changes to the application's appearance at runtime in correlation to its
 * {@link Category}.
 *
 * Usually this should be done via attributes and {@link android.view.ContextThemeWrapper}s.
 * In one case in Topeka it is more performant to work like this.
 * This case involves a trade-off between statically loading these themes versus inflation
 * in an adapter backed view without recycling.
 */
public enum Theme {
    topeka(R.color.topeka_primary, R.color.theme_blue_background,
            R.color.theme_blue_text, R.style.Topeka),
    blue(R.color.theme_blue_primary, R.color.theme_blue_background,
            R.color.theme_blue_text, R.style.Topeka_Blue),
    green(R.color.theme_green_primary, R.color.theme_green_background,
            R.color.theme_green_text, R.style.Topeka_Green),
    purple(R.color.theme_purple_primary, R.color.theme_purple_background,
            R.color.theme_purple_text, R.style.Topeka_Purple),
    red(R.color.theme_red_primary, R.color.theme_red_background,
            R.color.theme_red_text, R.style.Topeka_Red),
    yellow(R.color.theme_yellow_primary, R.color.theme_yellow_background,
            R.color.theme_yellow_text, R.style.Topeka_Yellow);

    private final int mColorPrimaryId;
    private final int mWindowBackgroundId;
    private final int mTextColorPrimaryId;
    private final int mStyleId;

    Theme(final int colorPrimaryId, final int windowBackgroundId,
            final int textColorPrimaryId, final int styleId) {
        mColorPrimaryId = colorPrimaryId;
        mWindowBackgroundId = windowBackgroundId;
        mTextColorPrimaryId = textColorPrimaryId;
        mStyleId = styleId;
    }

    @ColorRes
    public int getTextPrimaryColor() {
        return mTextColorPrimaryId;
    }

    @ColorRes
    public int getWindowBackgroundColor() {
        return mWindowBackgroundId;
    }

    @ColorRes
    public int getPrimaryColor() {
        return mColorPrimaryId;
    }

    @StyleRes
    public int getStyleId() {
        return mStyleId;
    }
}
