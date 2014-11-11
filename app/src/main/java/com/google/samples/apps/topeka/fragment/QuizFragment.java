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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.adapter.QuizPagerAdapter;
import com.google.samples.apps.topeka.helper.ViewHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;

/**
 * Encapsulates Quiz solving and displays it to the user.
 */
public class QuizFragment extends Fragment {

    private Category mCategory;

    public static QuizFragment newInstance(String categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("The category can not be null");
        }
        Bundle args = new Bundle();
        args.putString(Category.TAG, categoryId);
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String categoryId = getArguments().getString(Category.TAG);
        mCategory = TopekaDatabaseHelper.getCategoryWith(getActivity(), categoryId);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewPager = ViewHelper.getView(view, R.id.quiz_pager);
        viewPager.setBackgroundResource(mCategory.getTheme().getWindowBackgroundColor());
        viewPager.setAdapter(new QuizPagerAdapter(getActivity(), mCategory));
        super.onViewCreated(view, savedInstanceState);
    }
}