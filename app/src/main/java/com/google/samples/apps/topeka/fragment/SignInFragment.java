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

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.CategorySelectionActivity;
import com.google.samples.apps.topeka.adapter.AvatarAdapter;
import com.google.samples.apps.topeka.helper.ApiLevelHelper;
import com.google.samples.apps.topeka.helper.PreferencesHelper;
import com.google.samples.apps.topeka.helper.TransitionHelper;
import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.widget.TransitionListenerAdapter;

/**
 * Enable selection of an {@link Avatar} and user name.
 */
public class SignInFragment extends Fragment {

    private static final String ARG_EDIT = "EDIT";
    private static final String KEY_SELECTED_AVATAR_INDEX = "selectedAvatarIndex";
    private Player mPlayer;
    private EditText mFirstName;
    private EditText mLastInitial;
    private Avatar mSelectedAvatar;
    private View mSelectedAvatarView;
    private GridView mAvatarGrid;
    private FloatingActionButton mDoneFab;
    private boolean edit;

    public static SignInFragment newInstance(boolean edit) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_EDIT, edit);
        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            final int savedAvatarIndex = savedInstanceState.getInt(KEY_SELECTED_AVATAR_INDEX);
            if (savedAvatarIndex != GridView.INVALID_POSITION) {
                mSelectedAvatar = Avatar.values()[savedAvatarIndex];
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        contentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                setUpGridView(getView());
            }
        });
        return contentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mAvatarGrid != null) {
            outState.putInt(KEY_SELECTED_AVATAR_INDEX, mAvatarGrid.getCheckedItemPosition());
        } else {
            outState.putInt(KEY_SELECTED_AVATAR_INDEX, GridView.INVALID_POSITION);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        assurePlayerInit();
        checkIsInEditMode();

        if (mPlayer == null || edit) {
            view.findViewById(R.id.empty).setVisibility(View.GONE);
            view.findViewById(R.id.content).setVisibility(View.VISIBLE);
            initContentViews(view);
            initContents();
        } else {
            final Activity activity = getActivity();
            CategorySelectionActivity.start(activity, mPlayer);
            activity.finish();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void checkIsInEditMode() {
        final Bundle arguments = getArguments();
        //noinspection SimplifiableIfStatement
        if (arguments == null) {
            edit = false;
        } else {
            edit = arguments.getBoolean(ARG_EDIT, false);
        }
    }

    private void initContentViews(View view) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* no-op */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // hiding the floating action button if text is empty
                if (s.length() == 0) {
                    mDoneFab.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // showing the floating action button if avatar is selected and input data is valid
                if (isAvatarSelected() && isInputDataValid()) {
                    mDoneFab.show();
                }
            }
        };

        mFirstName = (EditText) view.findViewById(R.id.first_name);
        mFirstName.addTextChangedListener(textWatcher);
        mLastInitial = (EditText) view.findViewById(R.id.last_initial);
        mLastInitial.addTextChangedListener(textWatcher);
        mDoneFab = (FloatingActionButton) view.findViewById(R.id.done);
        mDoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.done:
                        savePlayer(getActivity());
                        removeDoneFab(new Runnable() {
                            @Override
                            public void run() {
                                if (null == mSelectedAvatarView) {
                                    performSignInWithTransition(mAvatarGrid.getChildAt(
                                            mSelectedAvatar.ordinal()));
                                } else {
                                    performSignInWithTransition(mSelectedAvatarView);
                                }
                            }
                        });
                        break;
                    default:
                        throw new UnsupportedOperationException(
                                "The onClick method has not been implemented for " + getResources()
                                        .getResourceEntryName(v.getId()));
                }
            }
        });
    }

    private void removeDoneFab(@Nullable Runnable endAction) {
        ViewCompat.animate(mDoneFab)
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withEndAction(endAction)
                .start();
    }

    private void setUpGridView(View container) {
        mAvatarGrid = (GridView) container.findViewById(R.id.avatars);
        mAvatarGrid.setAdapter(new AvatarAdapter(getActivity()));
        mAvatarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedAvatarView = view;
                mSelectedAvatar = Avatar.values()[position];
                // showing the floating action button if input data is valid
                if (isInputDataValid()) {
                    mDoneFab.show();
                }
            }
        });
        mAvatarGrid.setNumColumns(calculateSpanCount());
        if (mSelectedAvatar != null) {
            mAvatarGrid.setItemChecked(mSelectedAvatar.ordinal(), true);
        }
    }

    private void performSignInWithTransition(View v) {
        final Activity activity = getActivity();
        if (v == null || ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            // Don't run a transition if the passed view is null
            CategorySelectionActivity.start(activity, mPlayer);
            activity.finish();
            return;
        }

        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            activity.getWindow().getSharedElementExitTransition().addListener(
                    new TransitionListenerAdapter() {
                        @Override
                        public void onTransitionEnd(Transition transition) {
                            activity.finish();
                        }
                    });

            final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true,
                    new Pair<>(v, activity.getString(R.string.transition_avatar)));
            @SuppressWarnings("unchecked")
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pairs);
            CategorySelectionActivity.start(activity, mPlayer, activityOptions);
        }
    }

    private void initContents() {
        assurePlayerInit();
        if (mPlayer != null) {
            mFirstName.setText(mPlayer.getFirstName());
            mLastInitial.setText(mPlayer.getLastInitial());
            mSelectedAvatar = mPlayer.getAvatar();
        }
    }

    private void assurePlayerInit() {
        if (mPlayer == null) {
            mPlayer = PreferencesHelper.getPlayer(getActivity());
        }
    }

    private void savePlayer(Activity activity) {
        mPlayer = new Player(mFirstName.getText().toString(), mLastInitial.getText().toString(),
                mSelectedAvatar);
        PreferencesHelper.writeToPreferences(activity, mPlayer);
    }

    private boolean isAvatarSelected() {
        return mSelectedAvatarView != null || mSelectedAvatar != null;
    }

    private boolean isInputDataValid() {
        return PreferencesHelper.isInputDataValid(mFirstName.getText(), mLastInitial.getText());
    }

    /**
     * Calculates spans for avatars dynamically.
     *
     * @return The recommended amount of columns.
     */
    private int calculateSpanCount() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.size_fab);
        int avatarPadding = getResources().getDimensionPixelSize(R.dimen.spacing_double);
        return mAvatarGrid.getWidth() / (avatarSize + avatarPadding);
    }
}
