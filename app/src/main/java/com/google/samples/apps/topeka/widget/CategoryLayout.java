/*
 * Copyright 2014 Google Inc.
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

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;

/**
 * Custom {@link android.widget.LinearLayout} to wrap a
 * {@link com.google.samples.apps.topeka.model.Category}
 * view representation.
 */
public class CategoryLayout extends LinearLayout {

    private final ImageView mIcon;
    private final TextView mName;

    public CategoryLayout(Context context) {
        this(context, null);
    }

    public CategoryLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CategoryLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
        mIcon = initIcon(context, attrs, defStyle);
        mName = initName(context, attrs, defStyle);
        addView(mIcon, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mName, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private ImageView initIcon(Context context, AttributeSet attrs, int defStyle) {
        ImageView imageView = new ImageView(context, attrs, defStyle);
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_category_icon);
        imageView.setPadding(0, padding, 0, padding);
        return imageView;
    }

    private TextView initName(Context context, AttributeSet attrs, int defStyle) {
        TextView textView = new TextView(context, attrs, defStyle);
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_card_name);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public TextView getName() {
        return mName;
    }

    public void setImageResource(@DrawableRes int resId) {
        mIcon.setImageResource(resId);
    }

    public void setText(CharSequence text) {
        mName.setText(text);
    }
}
