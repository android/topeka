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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Theme;
import com.google.samples.apps.topeka.persistence.CategoryCursor;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

/**
 * An adapter backed by a {@link CategoryCursor} that allows display of {@link Category} data.
 */
public class CategoryCursorAdapter extends CursorAdapter {

    public static final String DRAWABLE = "drawable";
    private static final String ICON_CATEGORY = "icon_category_";
    private final Resources mResources;
    private final String mPackageName;
    private final LayoutInflater mLayoutInflater;

    public CategoryCursorAdapter(Activity activity) {
        super(activity, TopekaDatabaseHelper.getCategoryCursor(activity), true);
        mResources = activity.getResources();
        mPackageName = activity.getPackageName();
        mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.layout_category, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Category category = getCategoryOrThrow(cursor);
        Theme theme = category.getTheme();

        LinearLayout layout = (LinearLayout) view;
        ImageView icon = (ImageView) layout.findViewById(R.id.category_icon);
        setCategoryIcon(category, icon);
        icon.setBackgroundResource(theme.getWindowBackgroundColor());

        TextView title = (TextView) layout.findViewById(R.id.category_title);
        title.setText(category.getName());
        title.setTextColor(getColor(theme.getTextPrimaryColor()));
        title.setBackgroundResource(theme.getPrimaryColor());
    }

    private void setCategoryIcon(Category category, ImageView icon) {
        //TODO: 11/11/14 don't use resource lookup
        final int categoryImageResource = mResources.getIdentifier(
                ICON_CATEGORY + category.getId(), DRAWABLE, mPackageName);
        final boolean solved = category.isSolved();
        if (solved) {
            LayerDrawable solvedIcon = loadSolvedIcon(category, categoryImageResource);
            icon.setImageDrawable(solvedIcon);
        } else {
            icon.setImageResource(categoryImageResource);
        }
    }

    /**
     * Loads an icon that indicates that a category has already been solved.
     *
     * @param category The solved category to display.
     * @param categoryImageResource The category's identifying image.
     * @return The icon indicating that the category has been solved.
     */
    private LayerDrawable loadSolvedIcon(Category category, int categoryImageResource) {
        final Drawable done = loadTintedDoneDrawable();
        final Drawable categoryIcon = loadTintedCategoryDrawable(category, categoryImageResource);
        Drawable[] layers = new Drawable[]{categoryIcon, done}; // ordering is back to front
        return new LayerDrawable(layers);
    }

    /**
     * Loads and tints a drawable.
     *
     * @param category The category providing the tint color
     * @param categoryImageResource The image resource to tint
     * @return The tinted resource
     */
    private Drawable loadTintedCategoryDrawable(Category category, int categoryImageResource) {
        final Drawable categoryIcon = mResources.getDrawable(categoryImageResource);
        tintDrawable(categoryIcon, category.getTheme().getPrimaryColor());
        return categoryIcon;
    }

    /**
     * Loads and tints a check mark.
     *
     * @return The tinted check mark
     */
    private Drawable loadTintedDoneDrawable() {
        final Drawable done = mResources.getDrawable(R.drawable.ic_done);
        tintDrawable(done, android.R.color.white);
        return done;
    }

    /**
     * Convenience method for drawable tinting.
     *
     * @param drawable The drawable to tint.
     * @param colorRes The color resource id of the color used for tinting.
     */
    private void tintDrawable(Drawable drawable, @ColorRes int colorRes) {
        drawable.setTint(getColor(colorRes));
    }

    /**
     * Convenience method for color loading.
     *
     * @param colorRes The resource id of the color to load.
     * @return The loaded color.
     */
    private int getColor(@ColorRes int colorRes) {
        return mResources.getColor(colorRes);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    private Category getCategoryOrThrow(Cursor cursor) {
        final CategoryCursor categoryCursor = getCategoryCursorOrThrow(cursor);
        return categoryCursor.getCategory();
    }

    private CategoryCursor getCategoryCursorOrThrow(Cursor cursor) {
        if (cursor instanceof CategoryCursor) {
            return (CategoryCursor) cursor;
        } else {
            throw new UnsupportedOperationException(
                    "This adapter only works with an CategoryCursor");
        }
    }
}
