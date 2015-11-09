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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A transition that resizes text of a TextView.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TextResizeTransition extends Transition {

    private static final String PROPERTY_NAME_TEXT_RESIZE =
            "com.google.samples.apps.topeka.widget:TextResizeTransition:textSize";
    private static final String PROPERTY_NAME_PADDING_RESIZE =
            "com.google.samples.apps.topeka.widget:TextResizeTransition:paddingStart";

    private static final String[] TRANSITION_PROPERTIES = {PROPERTY_NAME_TEXT_RESIZE,
            PROPERTY_NAME_PADDING_RESIZE };

    public TextResizeTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (!(transitionValues.view instanceof TextView)) {
            throw new UnsupportedOperationException("Doesn't work on "
                    + transitionValues.view.getClass().getName());
        }
            TextView view = (TextView) transitionValues.view;
            transitionValues.values.put(PROPERTY_NAME_TEXT_RESIZE, view.getTextSize());
            transitionValues.values.put(PROPERTY_NAME_PADDING_RESIZE,
                    ViewCompat.getPaddingStart(view));
    }

    @Override
    public String[] getTransitionProperties() {
        return TRANSITION_PROPERTIES;
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }

        float initialTextSize = (float) startValues.values.get(PROPERTY_NAME_TEXT_RESIZE);
        float targetTextSize = (float) endValues.values.get(PROPERTY_NAME_TEXT_RESIZE);
        TextView targetView = (TextView) endValues.view;
        targetView.setTextSize(TypedValue.COMPLEX_UNIT_PX, initialTextSize);

        int initialPaddingStart = (int) startValues.values.get(PROPERTY_NAME_PADDING_RESIZE);
        int targetPaddingStart = (int) endValues.values.get(PROPERTY_NAME_PADDING_RESIZE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(targetView,
                        ViewUtils.PROPERTY_TEXT_SIZE,
                        initialTextSize,
                        targetTextSize),
                ObjectAnimator.ofInt(targetView,
                        ViewUtils.PROPERTY_TEXT_PADDING_START,
                        initialPaddingStart,
                        targetPaddingStart));
        return animatorSet;
    }
}
