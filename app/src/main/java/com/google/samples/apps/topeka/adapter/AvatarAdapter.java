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

package com.google.samples.apps.topeka.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.widget.outlineprovider.AvatarOutlineProvider;
import com.google.samples.apps.topeka.model.Avatar;

/**
 * Adapter to display {@link Avatar} icons.
 */
public class AvatarAdapter extends BaseAdapter {

    private static final Avatar[] mAvatars = Avatar.values();

    private final LayoutInflater mLayoutInflater;

    public AvatarAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.item_avatar, parent, false);
        }
        setAvatar((ImageView) convertView, mAvatars[position]);
        return convertView;
    }

    private void setAvatar(ImageView mIcon, Avatar avatar) {
        mIcon.setClipToOutline(true);
        mIcon.setOutlineProvider(new AvatarOutlineProvider());
        mIcon.setImageResource(avatar.getDrawableId());
        mIcon.setContentDescription(avatar.getNameForAccessibility());
    }

    @Override
    public int getCount() {
        return mAvatars.length;
    }

    @Override
    public Object getItem(int position) {
        return mAvatars[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
