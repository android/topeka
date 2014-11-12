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

import android.app.Activity;
import android.app.ActivityOptions;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toolbar;

import com.google.samples.apps.topeka.helper.PreferencesHelper;
import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.CategoryGridActivity;
import com.google.samples.apps.topeka.adapter.AvatarAdapter;
import com.google.samples.apps.topeka.helper.ViewHelper;
import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Player;

/**
 * Enables selection of an {@link Avatar} and user name.
 */
public class SignInFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private Player mPlayer;
    private EditText mFirstName;
    private EditText mLastName;
    private Avatar mSelectedAvatar = Avatar.ONE;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initContentViews(view);
        initPlayerCredentials();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initContentViews(View view) {
        ViewHelper.<Toolbar>getView(view, R.id.toolbar_sign_in).setTitle(R.string.sign_in);
        ViewHelper.<Toolbar>getView(view, R.id.toolbar_choose_avatar).setTitle(R.string.choose_avatar);
        mFirstName = ViewHelper.getView(view, R.id.first_name);
        mLastName = ViewHelper.getView(view, R.id.last_initial);
        ViewHelper.getView(view, R.id.check).setOnClickListener(this);
        setUpGridView(view);
    }

    private void setUpGridView(View view) {
        GridView gridView = ViewHelper.getView(view, R.id.avatars);
        gridView.setAdapter(new AvatarAdapter(getActivity()));
        gridView.setOnItemClickListener(this);
        gridView.setNumColumns(calculateSpanCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check:
                performSignIn(v);
                break;
            default:
                throw new UnsupportedOperationException(
                        "This click handler has not been implemented.");
        }
    }

    private void performSignIn(View v) {
        Activity activity = getActivity();
        mPlayer = new Player(mFirstName.getText().toString(), mLastName.getText().toString(),
                mSelectedAvatar);
        PreferencesHelper.writeToPreferences(activity, mPlayer);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, v,
                activity.getString(R.string.transition_avatar));
        CategoryGridActivity.start(activity, mPlayer, activityOptions);
    }


    private void initPlayerCredentials() {
        mPlayer = PreferencesHelper.getPlayer(getActivity());
        if (null != mPlayer) {
            mFirstName.setText(mPlayer.getFirstName());
            mLastName.setText(mPlayer.getLastInitial());
            mSelectedAvatar = mPlayer.getAvatar();
            //TODO: 10/28/14 keep avatar selected on GridView
        }
    }

    /**
     * Calculates spans for avatars dynamically.
     * @return The recommended amount of columns.
     */
    private int calculateSpanCount() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size);
        int defaultPadding = getResources().getDimensionPixelSize(R.dimen.padding_default);
        Point windowSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(windowSize);
        return windowSize.x / (avatarSize + defaultPadding * 2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedAvatar = Avatar.values()[position];
    }
}
