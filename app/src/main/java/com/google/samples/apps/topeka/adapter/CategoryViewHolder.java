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

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryViewHolder {

    private final ImageView mIcon;
    private final TextView mName;
    private final LinearLayout mContainer;
    private final CardView mCardView;

    public CategoryViewHolder(View itemView) {
        mIcon = (ImageView) itemView.findViewById(R.id.icon);
        mName = (TextView) itemView.findViewById(R.id.name);
        mContainer = (LinearLayout) itemView.findViewById(R.id.item_category_container);
        mCardView = (CardView) itemView.findViewById(R.id.card);
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public TextView getName() {
        return mName;
    }

    public LinearLayout getContainer() {
        return mContainer;
    }

    public CardView getCardView() {
        return mCardView;
    }
}