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

package com.google.samples.apps.topeka.helper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.transition.ChangeBounds;
import android.util.Property;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ViewUtils {

    private ViewUtils() {
        //no instance
    }

    public static final Property<FrameLayout, Integer> FOREGROUND_COLOR =
            new IntProperty<FrameLayout>("foregroundColor") {

                @Override
                public void setValue(FrameLayout layout, int value) {
                    if (layout.getForeground() instanceof ColorDrawable) {
                        ((ColorDrawable) layout.getForeground().mutate()).setColor(value);
                    } else {
                        layout.setForeground(new ColorDrawable(value));
                    }
                }

                @Override
                public Integer get(FrameLayout layout) {
                    if (layout.getForeground() instanceof ColorDrawable) {
                        return ((ColorDrawable) layout.getForeground()).getColor();
                    } else {
                        return Color.TRANSPARENT;
                    }
                }
            };

    public static final Property<View, Integer> BACKGROUND_COLOR =
            new IntProperty<View>("backgroundColor") {

                @Override
                public void setValue(View view, int value) {
                    view.setBackgroundColor(value);
                }

                @Override
                public Integer get(View view) {
                    Drawable d = view.getBackground();
                    if (d instanceof ColorDrawable) {
                        return ((ColorDrawable) d).getColor();
                    }
                    return Color.TRANSPARENT;
                }
            };

    /**
     * Allows changes to the text size in transitions and animations.
     * Using this with something else than {@link ChangeBounds}
     * can result in a severe performance penalty due to layout passes.
     */
    public static final Property<TextView, Float> PROPERTY_TEXT_SIZE =
            new FloatProperty<TextView>("textSize") {
                @Override
                public Float get(TextView view) {
                    return view.getTextSize();
                }

                @Override
                public void setValue(TextView view, float textSize) {
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
            };

    /**
     * Allows making changes to the start padding of a view.
     * Using this with something else than {@link ChangeBounds}
     * can result in a severe performance penalty due to layout passes.
     */
    public static final Property<TextView, Integer> PROPERTY_TEXT_PADDING_START =
            new IntProperty<TextView>("paddingStart") {
                @Override
                public Integer get(TextView view) {
                    return ViewCompat.getPaddingStart(view);
                }

                @Override
                public void setValue(TextView view, int paddingStart) {
                    ViewCompat.setPaddingRelative(view, paddingStart, view.getPaddingTop(),
                            ViewCompat.getPaddingEnd(view), view.getPaddingBottom());
                }
            };

    public static abstract class IntProperty<T> extends Property<T, Integer> {

        public IntProperty(String name) {
            super(Integer.class, name);
        }

        /**
         * A type-specific override of the {@link #set(Object, Integer)} that is faster when
         * dealing
         * with fields of type <code>int</code>.
         */
        public abstract void setValue(T object, int value);

        @Override
        final public void set(T object, Integer value) {
            //noinspection UnnecessaryUnboxing
            setValue(object, value.intValue());
        }
    }

    public static abstract class FloatProperty<T> extends Property<T, Float> {

        public FloatProperty(String name) {
            super(Float.class, name);
        }

        /**
         * A type-specific override of the {@link #set(Object, Float)} that is faster when dealing
         * with fields of type <code>int</code>.
         */
        public abstract void setValue(T object, float value);

        @Override
        final public void set(T object, Float value) {
            //noinspection UnnecessaryUnboxing
            setValue(object, value.floatValue());
        }
    }

    public static void setPaddingStart(TextView target, int paddingStart) {
        ViewCompat.setPaddingRelative(target, paddingStart, target.getPaddingTop(),
                ViewCompat.getPaddingEnd(target), target.getPaddingBottom());
    }

}
