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
package com.google.samples.apps.topeka.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

public class CategoryCursorAdapter extends CursorAdapter {

    public static final String ICON_CATEGORY = "icon_category_";
    public static final String THEME = "theme_";
    public static final String COLOR = "color";
    public static final String BACKGROUND = "_background";
    public static final String PRIMARY = "_primary";
    public static final String FOREGROUND = "_foreground";
    public static final String DRAWABLE = "drawable";

    public CategoryCursorAdapter(Activity activity) {
        super(activity, TopekaDatabaseHelper.getCategoryCursor(activity), true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor instanceof TopekaDatabaseHelper.CategoryCursor) {
            final TopekaDatabaseHelper.CategoryCursor categoryCursor
                    = (TopekaDatabaseHelper.CategoryCursor) cursor;
            final Category category = categoryCursor.getCategory();
            final Resources resources = context.getResources();
            final String packageName = context.getPackageName();
            final String categoryId = category.getId();

            CategoryViewHolder holder = new CategoryViewHolder(view);

            // Set contents
            holder.getIcon().setImageResource(resources.getIdentifier(
                    ICON_CATEGORY + categoryId, DRAWABLE, packageName));
            holder.getName().setText(category.getName());
            // Adjust styles
            adjustStyles(resources, category.getTheme().name(), packageName, holder);
        } else {
            throw new UnsupportedOperationException(
                    "This adapter only works with an CategoryCursor");
        }
    }

    private static void adjustStyles(Resources resources, String themeName, String packageName,
            CategoryViewHolder holder) {
        holder.getContainer().setBackgroundResource(resources.getIdentifier(
                THEME + themeName + BACKGROUND, COLOR, packageName));
        holder.getName().setBackgroundResource(resources.getIdentifier(
                THEME + themeName + PRIMARY, COLOR, packageName));
        holder.getName().setTextColor(resources.getColor(resources.getIdentifier(
                THEME + themeName + FOREGROUND, COLOR, packageName)));
    }

}
