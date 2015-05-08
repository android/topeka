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

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;

public class CategoryViewHolder {

    protected TextView title;
    protected ImageView icon;

    public CategoryViewHolder(LinearLayout container) {
        icon = (ImageView) container.findViewById(R.id.category_icon);
        title = (TextView) container.findViewById(R.id.category_title);
    }
}
