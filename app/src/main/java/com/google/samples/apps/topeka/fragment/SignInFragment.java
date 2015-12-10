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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.activity.CategorySelectionActivity;
import com.google.samples.apps.topeka.adapter.AvatarAdapter;
import com.google.samples.apps.topeka.helper.PreferencesHelper;
import com.google.samples.apps.topeka.helper.TransitionHelper;
import com.google.samples.apps.topeka.model.Avatar;
import com.google.samples.apps.topeka.model.Player;

/**
 * Enable selection of an {@link Avatar} and user name.
 */
public class SignInFragment extends Fragment {

    private static final String ARG_EDIT = "EDIT";
    private static final String KEY_AVATAR_POSITION = "avatarPosition";
    private Player mPlayer;
    private EditText mFirstName;
    private EditText mLastInitial;
    private Avatar mAvatar;
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
            final int savedAvatarPosition = savedInstanceState.getInt(KEY_AVATAR_POSITION);
            if (savedAvatarPosition != GridView.INVALID_POSITION) {
                mAvatar = Avatar.values()[savedAvatarPosition];
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
            outState.putInt(KEY_AVATAR_POSITION, mAvatarGrid.getCheckedItemPosition());
        } else {
            outState.putInt(KEY_AVATAR_POSITION, GridView.INVALID_POSITION);
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
                if (mAvatar != null && isInputDataValid()) {
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
                                performSignInWithTransition(getSharedViewForAvatarTransition());
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

    /**
     * Returns the view to be used as a shared view for the avatar transition.
     * <p/>
     * If the checked avatar has been scrolled out, returns the removed FAB as a last resort.
     *
     * @return The avatar view if it's displayed on screen, the FAB view otherwise.
     */
    private View getSharedViewForAvatarTransition() {
        // child position within the adapter's data set for the avatar when is displayed on screen
        final int childPosition = mAvatar.ordinal() - mAvatarGrid.getFirstVisiblePosition();
        // use the FAB as the initial view during the transition when the avatar is out of view
        if (childPosition < 0 || childPosition >= mAvatarGrid.getChildCount()) {
            return mDoneFab;
        }
        return mAvatarGrid.getChildAt(childPosition);
    }

    private void setUpGridView(View container) {
        mAvatarGrid = (GridView) container.findViewById(R.id.avatars);
        mAvatarGrid.setAdapter(new AvatarAdapter(getActivity()));
        mAvatarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAvatar = Avatar.values()[position];
                // showing the floating action button if input data is valid
                if (isInputDataValid()) {
                    mDoneFab.show();
                }
            }
        });
        mAvatarGrid.setNumColumns(calculateSpanCount());
        if (mAvatar != null) {
            mAvatarGrid.setItemChecked(mAvatar.ordinal(), true);
        }
    }

    private void performSignInWithTransition(View v) {
        final Activity activity = getActivity();

        final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true,
                new Pair<>(v, activity.getString(R.string.transition_avatar)));
        @SuppressWarnings("unchecked")
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, pairs);
        CategorySelectionActivity.start(activity, mPlayer, activityOptions);
    }

    private void initContents() {
        assurePlayerInit();
        if (mPlayer != null) {
            mFirstName.setText(mPlayer.getFirstName());
            mLastInitial.setText(mPlayer.getLastInitial());
            mAvatar = mPlayer.getAvatar();
        }
    }

    private void assurePlayerInit() {
        if (mPlayer == null) {
            mPlayer = PreferencesHelper.getPlayer(getActivity());
        }
    }

    private void savePlayer(Activity activity) {
        mPlayer = new Player(mFirstName.getText().toString(), mLastInitial.getText().toString(),
                mAvatar);
        PreferencesHelper.writeToPreferences(activity, mPlayer);
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
