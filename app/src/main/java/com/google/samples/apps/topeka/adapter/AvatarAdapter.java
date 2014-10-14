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
import android.app.ActivityOptions;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.QuizActivity;
import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Category;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarViewHolder> {

    private static final Avatar[] mAvatars = Avatar.values();

    protected final Activity mActivity;

    public AvatarAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public AvatarViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new AvatarViewHolder(LayoutInflater.from(mActivity)
                .inflate(R.layout.item_avatar, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final AvatarViewHolder holder, int position) {
        final Avatar avatar = mAvatars[position];
        holder.setImageResource(avatar.getDrawableId());
        holder.getIcon().setContentDescription(avatar.getNameForAccessibility());
    }

    @Override
    public int getItemCount() {
        return mAvatars.length;
    }
}
