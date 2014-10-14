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

package com.google.samples.apps.topeka.fragment;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.adapter.CategoryAdapter;
import com.google.samples.apps.topeka.model.Category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryGridFragment extends Fragment {

    private static final String EXTRA_CATEGORY = "Extra.Category";

    private static final int SPAN_COUNT = 2;

    public static CategoryGridFragment newInstance(Category[] categories) {
        checkArguments(categories);
        Bundle args = new Bundle();
        args.putParcelableArray(EXTRA_CATEGORY, categories);
        CategoryGridFragment categoryGridFragment = new CategoryGridFragment();
        categoryGridFragment.setArguments(args);
        return categoryGridFragment;
    }

    private static void checkArguments(Category[] categories) {
        if (null == categories || categories.length == 0) {
            throw new IllegalArgumentException(
                    "You'll need to provide categories in order for this to work.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView categoriesView = (RecyclerView) view.findViewById(R.id.categories);
        categoriesView.setLayoutManager(
                new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        Category[] categories = (Category[]) getArguments().getParcelableArray(EXTRA_CATEGORY);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories);
        categoriesView.setAdapter(adapter);
    }
}
