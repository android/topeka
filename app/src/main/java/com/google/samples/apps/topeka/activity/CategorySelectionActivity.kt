/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.topeka.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.samples.apps.topeka.R
import com.google.samples.apps.topeka.databinding.ActivityCategorySelectionBinding
import com.google.samples.apps.topeka.fragment.CategorySelectionFragment
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.helper.database
import com.google.samples.apps.topeka.helper.findFragmentById
import com.google.samples.apps.topeka.helper.getPlayer
import com.google.samples.apps.topeka.helper.isSignedIn
import com.google.samples.apps.topeka.helper.replaceFragment
import com.google.samples.apps.topeka.helper.savePlayer
import com.google.samples.apps.topeka.helper.signOut
import com.google.samples.apps.topeka.model.Player

class CategorySelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
                .setContentView<ActivityCategorySelectionBinding>(this,
                        R.layout.activity_category_selection)

        var player = intent.getParcelableExtra<Player>(EXTRA_PLAYER)
        if (!isSignedIn()) {
            if (player == null) {
                player = getPlayer()
            } else {
                savePlayer(player)
            }
        }
        binding.player = player
        setUpToolbar()
        if (savedInstanceState == null) {
            attachCategoryGridFragment()
        } else {
            setProgressBarVisibility(View.GONE)
        }
        supportPostponeEnterTransition()
    }

    private fun setUpToolbar() {
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar_player))
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun attachCategoryGridFragment() {
        replaceFragment(R.id.category_container,
                findFragmentById(R.id.category_container) ?: CategorySelectionFragment())
        setProgressBarVisibility(View.GONE)
    }

    private fun setProgressBarVisibility(visibility: Int) {
        findViewById<View>(R.id.progress).visibility = visibility
    }

    override fun onResume() {
        super.onResume()
        (findViewById<TextView>(R.id.score)).text =
                getString(R.string.x_points, database().getScore())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.sign_out) {
            handleSignOut()
            true
        } else super.onOptionsItemSelected(item)
    }

    @SuppressLint("NewApi")
    private fun handleSignOut() {
        signOut()
        database().reset()
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            window.exitTransition = TransitionInflater.from(this)
                    .inflateTransition(R.transition.category_enter)
        }
        SignInActivity.start(this)
        finish()
    }

    companion object {

        private val EXTRA_PLAYER = "player"

        fun start(activity: Activity, player: Player, options: ActivityOptionsCompat) {
            val starter = getStartIntent(activity, player)
            ActivityCompat.startActivity(activity, starter, options.toBundle())
        }

        fun start(context: Context, player: Player) {
            val starter = getStartIntent(context, player)
            context.startActivity(starter)
        }

        @VisibleForTesting
        fun getStartIntent(context: Context, player: Player): Intent {
            return Intent(context, CategorySelectionActivity::class.java).apply {
                putExtra(EXTRA_PLAYER, player)
            }
        }
    }
}

