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

package com.google.samples.apps.topeka.helper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.view.ViewCompat
import android.transition.ChangeBounds
import android.util.Property
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView

object ViewUtils {

    abstract class IntProperty<T>(name: String) : Property<T, Int>(Int::class.java, name) {

        /**
         * A type-specific override of the [.set] that is faster when
         * dealing
         * with fields of type `int`.
         */
        abstract fun setValue(type: T, value: Int)

        override fun set(type: T, value: Int?) = setValue(type, value!!.toInt())
    }

    abstract class FloatProperty<T>(name: String) : Property<T, Float>(Float::class.java, name) {

        /**
         * A type-specific override of the [.set] that is faster when dealing
         * with fields of type `int`.
         */
        abstract fun setValue(type: T, value: Float)

        override fun set(type: T, value: Float?) = setValue(type, value!!.toFloat())
    }

    fun setPaddingStart(target: TextView, paddingStart: Int) =
            ViewCompat.setPaddingRelative(target, paddingStart, target.paddingTop,
                    ViewCompat.getPaddingEnd(target), target.paddingBottom)

}

/**
 * Allows making changes to the start padding of a view.
 * Using this with something else than [ChangeBounds]
 * can result in a severe performance penalty due to layout passes.
 */
val TextView.PADDING_START: Property<TextView, Int>
    get() = object :
            ViewUtils.IntProperty<TextView>("paddingStart") {
        override fun get(view: TextView) = ViewCompat.getPaddingStart(view)

        override fun setValue(view: TextView, paddingStart: Int) =
                ViewCompat.setPaddingRelative(view, paddingStart, view.paddingTop,
                        ViewCompat.getPaddingEnd(view), view.paddingBottom)
    }

/**
 * Allows changes to the text size in transitions and animations.
 * Using this with something else than [ChangeBounds]
 * can result in a severe performance penalty due to layout passes.
 */
val TextView.TEXT_SIZE: Property<TextView, Float>
    get() = object :
            ViewUtils.FloatProperty<TextView>("textSize") {
        override fun get(view: TextView) = view.textSize

        override fun setValue(view: TextView, textSize: Float) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }
    }


val FrameLayout.FOREGROUND_COLOR: Property<FrameLayout, Int>
    get() = object :
            ViewUtils.IntProperty<FrameLayout>("foregroundColor") {

        override fun setValue(layout: FrameLayout, value: Int) {
            if (layout.foreground is ColorDrawable) {
                (layout.foreground.mutate() as ColorDrawable).color = value
            } else {
                layout.foreground = ColorDrawable(value)
            }
        }

        override fun get(layout: FrameLayout): Int? {
            return if (layout.foreground is ColorDrawable) {
                (layout.foreground as ColorDrawable).color
            } else {
                Color.TRANSPARENT
            }
        }
    }

val View.BACKGROUND_COLOR: Property<View, Int>
    get() = object : ViewUtils.IntProperty<View>("backgroundColor") {

        override fun setValue(view: View, value: Int) = view.setBackgroundColor(value)

        override fun get(view: View) = (view.background as? ColorDrawable)?.color
                ?: Color.TRANSPARENT
    }

/**
 * Performs a given action when a layout change happens.
 */
inline fun View.onLayoutChange(crossinline action: View.() -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(v: View, left: Int, top: Int,
                                    right: Int, bottom: Int,
                                    oldLeft: Int, oldTop: Int,
                                    oldRight: Int, oldBottom: Int) {
            removeOnLayoutChangeListener(this)
            action()
        }
    })
}

/**
 * Performs a given action before the view tree is drawing.
 */
inline fun View.beforeDrawing(drawAfterAction: Boolean = true, crossinline action: View.() -> Unit) {
    viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    action()
                    return drawAfterAction
                }
            })
}
