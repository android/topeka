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

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * A simple adapter to display a options of a quiz.
 */
public class OptionsQuizAdapter extends BaseAdapter {

    private final String[] mOptions;
    private final int mLayoutId;

    /**
     * Creates an {@link OptionsQuizAdapter}.
     *
     * @param options The options to add to the adapter.
     * @param layoutId Must consist of a single {@link TextView}.
     */
    public OptionsQuizAdapter(String[] options, @LayoutRes int layoutId) {
        mOptions = options;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mOptions.length;
    }

    @Override
    public String getItem(int position) {
        return mOptions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        /* Important to return tru ein order to get checked items from this adapter correctly */
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(mLayoutId, parent, false);
        }
        ((TextView) convertView).setText(getItem(position));
        return convertView;
    }
}
