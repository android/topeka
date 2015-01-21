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
import com.google.samples.apps.topeka.activity.QuizActivity;
import com.google.samples.apps.topeka.adapter.CategoryAdapter;
import com.google.samples.apps.topeka.model.Category;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class CategoryGridFragment extends Fragment implements AdapterView.OnItemClickListener {

    private CategoryAdapter mCategoryAdapter;

    public static CategoryGridFragment newInstance() {
        return new CategoryGridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpQuizGrid((GridView) view.findViewById(R.id.categories));
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpQuizGrid(GridView categoriesView) {
        categoriesView.setOnItemClickListener(this);
        mCategoryAdapter = new CategoryAdapter(getActivity());
        categoriesView.setAdapter(mCategoryAdapter);
    }

    @Override
    public void onResume() {
        mCategoryAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity activity = getActivity();
        //TODO: finalize the animations
        ImageView iconView = (ImageView) view.findViewById(R.id.category_icon);
        startQuizActivityWithTransition(activity, iconView, mCategoryAdapter.getItem(position));
    }

    private void startQuizActivityWithTransition(Activity activity, ImageView iconView,
            Category category) {
        // Creates pair of transition participants.
        Pair participants = new Pair<View, String>(iconView,
                activity.getString(R.string.transition_background));
        ActivityOptions sceneTransitionAnimation = ActivityOptions
                .makeSceneTransitionAnimation(activity, participants);
        // Starts the activity with the participants, animating from one to the other.
        final Bundle transitionBundle = sceneTransitionAnimation.toBundle();
        activity.startActivity(QuizActivity.getStartIntent(activity, category), transitionBundle);
    }
}
