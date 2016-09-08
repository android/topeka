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

package com.google.samples.apps.topeka.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.ApiLevelHelper;
import com.google.samples.apps.topeka.widget.outlineprovider.RoundOutlineProvider;

/**
 * A simple view that wraps an avatar.
 */
public class AvatarView extends ImageView implements Checkable {

    private boolean mChecked;
    private static final int NOT_FOUND = 0;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarView, defStyle, 0);
        try {
            final int avatarDrawableId = a.getResourceId(R.styleable.AvatarView_avatar, NOT_FOUND);
            if (avatarDrawableId != NOT_FOUND) {
                setAvatar(avatarDrawableId);
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setChecked(boolean b) {
        mChecked = b;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    /**
     * Set the image for this avatar. Will be used to create a round version of this avatar.
     *
     * @param resId The image's resource id.
     */
    @SuppressLint("NewApi")
    public void setAvatar(@DrawableRes int resId) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            setClipToOutline(true);
            setImageResource(resId);
        } else {
            setAvatarPreLollipop(resId);
        }
    }

    private void setAvatarPreLollipop(@DrawableRes int resId) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), resId,
                getContext().getTheme());
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        @SuppressWarnings("ConstantConditions")
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(),
                bitmapDrawable.getBitmap());
        roundedDrawable.setCircular(true);
        setImageDrawable(roundedDrawable);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            Drawable border = ContextCompat.getDrawable(getContext(), R.drawable.selector_avatar);
            border.setBounds(0, 0, getWidth(), getHeight());
            border.draw(canvas);
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            return;
        }
        if (w > 0 && h > 0) {
            setOutlineProvider(new RoundOutlineProvider(Math.min(w, h)));
        }
    }
}
