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
package com.google.samples.apps.topeka.helper;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

/**
 * Collection of shared methods for use in activities.
 */
public class ActivityHelper {

    private static final int FINISH_DELAY = 500; // milliseconds
    private final static Handler HANDLER = new Handler(Looper.getMainLooper());

    private ActivityHelper() {
        //no instance
    }

    public static void setStatusAndNavigationBarColor(Activity activity, int colorPrimary) {
        if (null != activity) {
            activity.getWindow().setStatusBarColor(colorPrimary);
            activity.getWindow().setNavigationBarColor(colorPrimary);
        }
    }

    /**
     * Finishes an {@link Activity} after a short time instead of immediately.
     *
     * @param activity The activity to finish.
     */
    public static void finishDelayed(final Activity activity) {
        Runnable finisher = new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        };
        HANDLER.postDelayed(finisher, FINISH_DELAY);
    }
}
