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

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.adapter.AvatarAdapter;

public class SignInFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getToolbar(view, R.id.toolbar_sign_in).setTitle(R.string.sign_in);
        getToolbar(view, R.id.toolbar_choose_avatar).setTitle(R.string.choose_avatar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.avatars);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), calculateSpanCount(),
                GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AvatarAdapter(getActivity()));
        super.onViewCreated(view, savedInstanceState);
    }

    private int calculateSpanCount() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size);
        int defaultPadding = getResources().getDimensionPixelSize(R.dimen.padding_default);

        Point windowSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(windowSize);

        return windowSize.x / (avatarSize + defaultPadding * 2);
    }

    private Toolbar getToolbar(View view, int resId) {
        return (Toolbar) view.findViewById(resId);
    }
}
