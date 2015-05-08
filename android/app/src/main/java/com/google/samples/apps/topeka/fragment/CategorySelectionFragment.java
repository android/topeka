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

package com.google.samples.apps.topeka.fragment;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.QuizActivity;
import com.google.samples.apps.topeka.adapter.CategoryAdapter;
import com.google.samples.apps.topeka.model.Category;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class CategorySelectionFragment extends Fragment {

    private CategoryAdapter mCategoryAdapter;

    public static CategorySelectionFragment newInstance() {
        return new CategorySelectionFragment();
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
        categoriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = getActivity();
                startQuizActivityWithTransition(activity, view.findViewById(R.id.category_title),
                        mCategoryAdapter.getItem(position));
            }
        });
        mCategoryAdapter = new CategoryAdapter(getActivity());
        categoriesView.setAdapter(mCategoryAdapter);
    }

    @Override
    public void onResume() {
        mCategoryAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void startQuizActivityWithTransition(Activity activity, View toolbar,
            Category category) {

        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        View decor = activity.getWindow().getDecorView();
        View statusBar = decor.findViewById(android.R.id.statusBarBackground);
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // Create pair of transition participants.
        List<Pair> participants = new ArrayList<>(3);
        participants.add(new Pair<>(toolbar, activity.getString(R.string.transition_toolbar)));
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        @SuppressWarnings("unchecked")
        ActivityOptions sceneTransitionAnimation = ActivityOptions
                .makeSceneTransitionAnimation(activity,
                        participants.toArray(new Pair[participants.size()]));

        // Starts the activity with the participants, animating from one to the other.
        final Bundle transitionBundle = sceneTransitionAnimation.toBundle();
        activity.startActivity(QuizActivity.getStartIntent(activity, category), transitionBundle);
    }

    private void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }
}
