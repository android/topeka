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

package com.google.samples.apps.topeka.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.fragment.CategorySelectionFragment;
import com.google.samples.apps.topeka.helper.PreferencesHelper;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;
import com.google.samples.apps.topeka.widget.AvatarView;

public class CategorySelectionActivity extends AppCompatActivity {

    private static final String EXTRA_PLAYER = "player";

    public static void start(Activity activity, Player player, ActivityOptionsCompat options) {
        Intent starter = getStartIntent(activity, player);
        ActivityCompat.startActivity(activity, starter, options.toBundle());
    }

    public static void start(Context context, Player player) {
        Intent starter = getStartIntent(context, player);
        context.startActivity(starter);
    }

    @NonNull
    static Intent getStartIntent(Context context, Player player) {
        Intent starter = new Intent(context, CategorySelectionActivity.class);
        starter.putExtra(EXTRA_PLAYER, player);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_selection);
        Player player = getIntent().getParcelableExtra(EXTRA_PLAYER);
        if (!PreferencesHelper.isSignedIn(this) && player != null) {
            PreferencesHelper.writeToPreferences(this, player);
        }
        setUpToolbar(player);
        if (savedInstanceState == null) {
            attachCategoryGridFragment();
        } else {
            setProgressBarVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView scoreView = (TextView) findViewById(R.id.score);
        final int score = TopekaDatabaseHelper.getScore(this);
        scoreView.setText(getString(R.string.x_points, score));
    }

    private void setUpToolbar(Player player) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final AvatarView avatarView = (AvatarView) toolbar.findViewById(R.id.avatar);
        avatarView.setAvatar(player.getAvatar().getDrawableId());
        //noinspection PrivateResource
        ((TextView) toolbar.findViewById(R.id.title)).setText(getDisplayName(player));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out: {
                signOut();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        PreferencesHelper.signOut(this);
        TopekaDatabaseHelper.reset(this);
        SignInActivity.start(this, false, null);
        ActivityCompat.finishAfterTransition(this);
    }

    private String getDisplayName(Player player) {
        return getString(R.string.player_display_name, player.getFirstName(),
                player.getLastInitial());
    }

    private void attachCategoryGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.quiz_container, CategorySelectionFragment.newInstance())
                .commit();
        setProgressBarVisibility(View.GONE);
    }

    private void setProgressBarVisibility(int visibility) {
        findViewById(R.id.progress).setVisibility(visibility);
    }
}

