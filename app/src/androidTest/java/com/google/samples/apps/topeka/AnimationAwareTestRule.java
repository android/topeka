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

package com.google.samples.apps.topeka;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A test rule that will fail a test if animations or transitions are enabled on a device.
 */
public class AnimationAwareTestRule implements TestRule {

    private static final String TAG = "AnimationAware";

    public AnimationAwareTestRule() { }

    @Override
    public Statement apply(Statement base, Description description) {
        return new AnimationAwareStatement(base);
    }

    private class AnimationAwareStatement extends Statement {

        private Statement mBase;

        public AnimationAwareStatement(Statement base) {
            mBase = base;
        }

        @Override
        public void evaluate() throws Throwable {
            checkForDisabledAnimationsAndTransitions(InstrumentationRegistry.getTargetContext());
            mBase.evaluate();
        }
    }

    private static void checkForDisabledAnimationsAndTransitions(Context context)
            throws AnimationsEnabledException {

        ContentResolver resolver = context.getContentResolver();
        final float transitionAnimationScale = getTransitionAnimationScale(resolver);
        final float windowAnimationScale = getWindowAnimationScale(resolver);
        final float animatorDurationScale = getAnimatorDurationScale(resolver);

        final boolean isEnabled = transitionAnimationScale + animatorDurationScale +
                windowAnimationScale != 0;

        if (isEnabled) {
            throw new AnimationsEnabledException();
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InlinedApi")
    private static float getTransitionAnimationScale(ContentResolver resolver) {
        //noinspection deprecation
        return getSetting(resolver, Settings.Global.TRANSITION_ANIMATION_SCALE,
                Settings.System.TRANSITION_ANIMATION_SCALE);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InlinedApi")
    private static float getWindowAnimationScale(ContentResolver resolver) {
        //noinspection deprecation
        return getSetting(resolver, Settings.Global.WINDOW_ANIMATION_SCALE,
                Settings.System.WINDOW_ANIMATION_SCALE);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InlinedApi")
    private static float getAnimatorDurationScale(ContentResolver resolver) {
        return getSetting(resolver, Settings.Global.ANIMATOR_DURATION_SCALE,
                Settings.System.ANIMATOR_DURATION_SCALE);
    }

    private static float getSetting(ContentResolver resolver, String current, String deprecated) {
        if (isJellyBeanOrHigher()) {
            return getGlobalSetting(resolver, current);
        } else {
            //noinspection deprecation
            return getSystemSetting(resolver, deprecated);
        }
    }

    private static boolean isJellyBeanOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static float getGlobalSetting(ContentResolver resolver, String setting) {
        try {
            return Settings.Global.getFloat(resolver, setting);
        } catch (Settings.SettingNotFoundException e) {
            Log.w(TAG, "getSystemSetting: Setting not found", e);
            return 0;
        }
    }

    private static float getSystemSetting(ContentResolver resolver, String setting) {
        try {
            return Settings.System.getFloat(resolver, setting);
        } catch (Settings.SettingNotFoundException e) {
            Log.w(TAG, "getSystemSetting: Setting not found", e);
            return 0;
        }
    }

    /**
     * Error that's being thrown when your system has animations enabled and should not.
     */
    private static class AnimationsEnabledException extends RuntimeException {
        public AnimationsEnabledException() {
            super("To properly execute this test, disable animations and transitions " +
                    "on your device.\n" +
                    "For more info check: http://goo.gl/qVu1yV");
        }
    }
}
