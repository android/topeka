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

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Shared methods for views.
 */
public class ViewHelper {

    /**
     * Convenience method to find a view from it's parent by resource id.
     *
     * @param parentView View containing the target view.
     * @param resId Target view's resource identifier.
     * @param <T> Expected view type.
     * @return Found view.
     */
    public static <T extends View> T getView(View parentView, @IdRes int resId) {
        View view = parentView.findViewById(resId);
        return (T) view;
    }

    /**
     * Inflates a layout resource with a given parent and does <b>not attach it</b> to the parent.
     *
     * @return The newly inflated view.
     */
    public static <T extends View> T inflate(ViewGroup parentView, @LayoutRes int resId) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parentView.getContext());
        return (T) layoutInflater.inflate(resId, parentView, false);
    }
}
