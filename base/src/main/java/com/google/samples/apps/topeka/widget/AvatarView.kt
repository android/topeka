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

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.Checkable

import com.google.samples.apps.topeka.base.R
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.widget.outlineprovider.RoundOutlineProvider

/**
 * A simple view that wraps an avatar.
 */
class AvatarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), Checkable {

    private var isChecked = false

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AvatarView, defStyle, 0)
        try {
            val avatarDrawableId = attributes
                    .getResourceId(R.styleable.AvatarView_avatar, NOT_FOUND)
            if (avatarDrawableId != NOT_FOUND) {
                setAvatar(avatarDrawableId)
            }
        } finally {
            attributes.recycle()
        }
    }

    override fun setChecked(b: Boolean) {
        isChecked = b
        invalidate()
    }

    override fun isChecked() = isChecked

    override fun toggle() = setChecked(!isChecked)

    fun setAvatar(avatar: Avatar) {
        setAvatar(avatar.drawableId)
    }

    /**
     * Set the image for this avatar. Will be used to create a round version of this avatar.

     * @param resId The image's resource id.
     */
    @SuppressLint("NewApi")
    fun setAvatar(@DrawableRes resId: Int) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            clipToOutline = true
            setImageResource(resId)
        } else {
            setAvatarPreLollipop(resId)
        }
    }

    private fun setAvatarPreLollipop(@DrawableRes resId: Int) {
        val drawable = ResourcesCompat.getDrawable(resources, resId, context.theme)
                as BitmapDrawable
        val roundedDrawable = RoundedBitmapDrawableFactory.create(resources, drawable.bitmap)
                .apply {
                    isCircular = true
                }
        setImageDrawable(roundedDrawable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isChecked) {
            ContextCompat.getDrawable(context, R.drawable.selector_avatar)!!.apply {
                setBounds(0, 0, width, height)
                draw(canvas)
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            return
        }
        if (w > 0 && h > 0) {
            outlineProvider = RoundOutlineProvider(Math.min(w, h))
        }
    }

    companion object {
        private val NOT_FOUND = 0
    }
}
