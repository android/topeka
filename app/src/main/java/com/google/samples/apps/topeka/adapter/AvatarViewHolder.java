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

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.samples.apps.topeka.AvatarOutlineProvider;
import com.google.samples.apps.topeka.R;


public class AvatarViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mIcon;

    public AvatarViewHolder(View view) {
        super(view);
        mIcon = (ImageView) view.findViewById(R.id.avatar);
        mIcon.setClipToOutline(true);
        mIcon.setOutlineProvider(new AvatarOutlineProvider());
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        mIcon.setOnClickListener(clickListener);
    }

    public void setImageResource(@DrawableRes int drawableResId) {
        mIcon.setImageResource(drawableResId);
    }
}