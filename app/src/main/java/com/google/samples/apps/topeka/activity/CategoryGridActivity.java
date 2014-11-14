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

package com.google.samples.apps.topeka.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.fragment.CategoryGridFragment;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;
import com.google.samples.apps.topeka.widget.outlineprovider.ToolbarIconOutlineProvider;

public class CategoryGridActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private static final String EXTRA_PLAYER = "player";
    private Toolbar mToolbar;

    public static void start(Context context, Player player, ActivityOptions options) {
        Intent starter = new Intent(context, CategoryGridActivity.class);
        starter.putExtra(EXTRA_PLAYER, player);
        context.startActivity(starter, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selection);
        Player player = getIntent().getParcelableExtra(EXTRA_PLAYER);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_player);
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
        scoreView.setText(String.valueOf(score));
    }

    private void setUpToolbar(Player player) {
        ImageView avatarView = (ImageView) findViewById(R.id.avatar);
        avatarView.setClipToOutline(true);
        avatarView.setOutlineProvider(new ToolbarIconOutlineProvider());
        avatarView.setImageResource(player.getAvatar().getDrawableId());
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(getDisplayName(player));
    }

    private String getDisplayName(Player player) {
        return getString(R.string.player_display_name, player.getFirstName(),
                player.getLastInitial());
    }

    private void attachCategoryGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.quiz_container, CategoryGridFragment.newInstance())
                .commit();
        setProgressBarVisibility(View.GONE);
    }

    private void setProgressBarVisibility(int visibility) {
        findViewById(R.id.progress).setVisibility(visibility);
    }
}

