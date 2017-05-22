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

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v4.view.ViewCompat
import android.transition.Transition
import android.transition.TransitionValues
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.google.samples.apps.topeka.helper.PADDING_START
import com.google.samples.apps.topeka.helper.TEXT_SIZE

/**
 * A transition that re-sizes text of a TextView.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class TextResizeTransition(context: Context, attrs: AttributeSet) : Transition(context, attrs) {

    override fun captureStartValues(values: TransitionValues) = captureValues(values)

    override fun captureEndValues(values: TransitionValues) = captureValues(values)

    private fun captureValues(values: TransitionValues) {
        if (values.view !is TextView) {
            throw UnsupportedOperationException("Doesn't work on ${values.view.javaClass.name}")
        }
        val view = values.view as TextView
        with(values.values) {
            put(PROPERTY_NAME_TEXT_RESIZE, view.textSize)
            put(PROPERTY_NAME_PADDING_RESIZE, ViewCompat.getPaddingStart(view))
        }
    }

    override fun getTransitionProperties() = TRANSITION_PROPERTIES

    override fun createAnimator(sceneRoot: ViewGroup,
                                startValues: TransitionValues?,
                                endValues: TransitionValues?): Animator? {
        if (startValues == null || endValues == null) {
            return null
        }

        val initialTextSize = startValues.values[PROPERTY_NAME_TEXT_RESIZE] as Float
        val targetTextSize = endValues.values[PROPERTY_NAME_TEXT_RESIZE] as Float
        val targetView = (endValues.view as TextView).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, initialTextSize)
        }

        val initialPaddingStart = startValues.values[PROPERTY_NAME_PADDING_RESIZE] as Int
        val targetPaddingStart = endValues.values[PROPERTY_NAME_PADDING_RESIZE] as Int
        return AnimatorSet()
                .apply {
                    playTogether(
                            ObjectAnimator.ofFloat(targetView,
                                    targetView.TEXT_SIZE,
                                    initialTextSize,
                                    targetTextSize),
                            ObjectAnimator.ofInt(targetView,
                                    targetView.PADDING_START,
                                    initialPaddingStart,
                                    targetPaddingStart))
                }
    }

    companion object {

        private const val PROPERTY_NAME_TEXT_RESIZE = "TextResizeTransition:textSize"
        private const val PROPERTY_NAME_PADDING_RESIZE = "TextResizeTransition:paddingStart"

        private val TRANSITION_PROPERTIES = arrayOf(PROPERTY_NAME_TEXT_RESIZE,
                PROPERTY_NAME_PADDING_RESIZE)
    }
}
