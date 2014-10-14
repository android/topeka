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

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.QuizActivity;
import com.google.samples.apps.topeka.model.Category;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    public static final String ICON_CATEGORY = "icon_category_";
    public static final String THEME = "theme_";
    public static final String COLOR = "color";
    public static final String BACKGROUND = "_background";
    public static final String PRIMARY = "_primary";
    public static final String FOREGROUND = "_foreground";
    public static final String DRAWABLE = "drawable";
    protected final Activity mActivity;
    private final Category[] mCategories;

    public CategoryAdapter(Activity activity, Category[] categories) {
        mActivity = activity;
        mCategories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(mActivity)
                .inflate(R.layout.item_category, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        final Category category = mCategories[position];
        final Resources resources = mActivity.getResources();
        final String packageName = mActivity.getPackageName();
        final String categoryId = category.getId();

        // Set contents
        holder.getIcon().setImageResource(resources.getIdentifier(
                ICON_CATEGORY + categoryId, DRAWABLE, packageName));
        holder.getName().setText(category.getName());
        // Adjust styles
        adjustStyles(resources, category.getTheme().name(), packageName, holder);
        // Bind event handlers
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO finalize the animations
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        mActivity, mActivity.findViewById(R.id.name),
                        mActivity.getString(R.string.transition_background));

                mActivity.startActivity(QuizActivity.getStartIntent(mActivity, category),
                        activityOptions.toBundle());
            }
        });
    }

    private void adjustStyles(Resources resources, String themeName, String packageName,
            CategoryViewHolder holder) {
        holder.getContainer().setBackgroundResource(resources.getIdentifier(
                THEME + themeName + BACKGROUND, COLOR, packageName));
        holder.getName().setBackgroundResource(resources.getIdentifier(
                THEME + themeName + PRIMARY, COLOR, packageName));
        holder.getName().setTextColor(resources.getColor(resources.getIdentifier(
                THEME + themeName + FOREGROUND, COLOR, packageName)));
    }

    @Override
    public int getItemCount() {
        return mCategories.length;
    }
}
