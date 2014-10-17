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
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.samples.apps.topeka.PreferencesHelper;
import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.QuizSelectionActivity;
import com.google.samples.apps.topeka.adapter.AvatarAdapter;
import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.widget.PlayArrow;

public class SignInFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private Player mPlayer;
    private GridView mGridView;
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
        getToolbar(view, R.id.toolbar_sign_in).setTitle(R.string.sign_in);
        getToolbar(view, R.id.toolbar_choose_avatar).setTitle(R.string.choose_avatar);
        initViews(view);
        initPlayerCredentials();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews(View view) {
        mFirstName = getView(view, R.id.first_name);
        mLastName = getView(view, R.id.last_initial);
        PlayArrow check = getView(view, R.id.check);
        check.setOnClickListener(this);
        mGridView = (GridView) view.findViewById(R.id.avatars);
        mGridView.setAdapter(new AvatarAdapter(getActivity()));
        mGridView.setOnItemClickListener(this);
        mGridView.setNumColumns(calculateSpanCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check:
                mPlayer = new Player(mFirstName.getText().toString(),
                        mLastName.getText().toString(), mSelectedAvatar);
                PreferencesHelper.writeToPreferences(getActivity(), mPlayer);
                QuizSelectionActivity.start(getActivity(), mPlayer);
                break;
            default:
                throw new UnsupportedOperationException(
                        "This click handler has not been implemented.");
        }
    }



    private void initPlayerCredentials() {
        mPlayer = PreferencesHelper.getPlayer(getActivity());
        if (null != mPlayer) {
            mFirstName.setText(mPlayer.getFirstName());
            mLastName.setText(mPlayer.getLastInitial());
            mGridView.setSelection(mPlayer.getAvatar().ordinal());
        }
    }

    private <T extends View> T getView(View parentView, @IdRes int resId) {
        View view = parentView.findViewById(resId);
        return (T) view;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedAvatar = Avatar.values()[position];
    }
}
