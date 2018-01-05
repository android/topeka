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

package com.google.samples.apps.topeka.fragment

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import com.google.samples.apps.topeka.adapter.AvatarAdapter
import com.google.samples.apps.topeka.base.R
import com.google.samples.apps.topeka.helper.ActivityLaunchHelper
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.helper.DefaultLogin
import com.google.samples.apps.topeka.helper.TAG
import com.google.samples.apps.topeka.helper.TransitionHelper
import com.google.samples.apps.topeka.helper.isLoggedIn
import com.google.samples.apps.topeka.helper.login
import com.google.samples.apps.topeka.helper.onLayoutChange
import com.google.samples.apps.topeka.helper.onSmartLockResult
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.model.Player
import com.google.samples.apps.topeka.widget.TextWatcherAdapter
import com.google.samples.apps.topeka.widget.TransitionListenerAdapter

/**
 * Enable selection of an [Avatar] and user name.
 */
class SignInFragment : Fragment() {

    private var firstNameView: EditText? = null
    private var lastInitialView: EditText? = null
    private var doneFab: FloatingActionButton? = null
    private var avatarGrid: GridView? = null

    private val edit by lazy { arguments?.getBoolean(ARG_EDIT, false) ?: false }

    private var selectedAvatarView: View? = null
    private var player: Player? = null
    private var selectedAvatar: Avatar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val avatarIndex = savedInstanceState.getInt(KEY_SELECTED_AVATAR_INDEX)
            if (avatarIndex != GridView.INVALID_POSITION) {
                selectedAvatar = Avatar.values()[avatarIndex]
            }
        }

        activity?.run {
            if (isLoggedIn()) {
                navigateToCategoryActivity()
            } else {
                login.loginPlayer(this, ::onSuccessfulLogin)
            }
        }
        super.onCreate(savedInstanceState)
    }

    /**
     * Called when logged in successfully.
     */
    private fun onSuccessfulLogin(player: Player) {
        if (login != DefaultLogin) return
        this.player = player
        if (edit) {
            with(player) {
                firstNameView?.setText(player.firstName)
                lastInitialView?.run {
                    setText(player.lastInitial)
                    requestFocus()
                    setSelection(length())
                }
                this@SignInFragment.player = player.also {
                    if (activity != null)
                        login.savePlayer(activity!!, this, { selectAvatar(it.avatar!!) })
                }
            }
        } else {
            navigateToCategoryActivity()
        }
    }

    private fun navigateToCategoryActivity() {
        activity?.run {
            ActivityLaunchHelper.launchCategorySelection(this)
            supportFinishAfterTransition()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activity?.onSmartLockResult(
                requestCode,
                resultCode,
                data,
                success = {
                    player = it
                    initContents()
                    navigateToCategoryActivity()
                },
                failure = {
                    activity?.run {
                        login.loginPlayer(this, ::onSuccessfulLogin)
                    }
                }
        )
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_sign_in, container, false)
        contentView.onLayoutChange {
            avatarGrid?.apply {
                adapter = AvatarAdapter(activity!!)
                onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
                    selectedAvatarView = view
                    selectedAvatar = Avatar.values()[position]
                    // showing the floating action button if input data is valid
                    showFab()
                }
                numColumns = calculateSpanCount()
                selectedAvatar?.run { selectAvatar(this) }
            }
        }
        return contentView
    }

    /**
     * Calculates spans for avatars dynamically.

     * @return The recommended amount of columns.
     */
    private fun calculateSpanCount(): Int {
        val avatarSize = resources.getDimensionPixelSize(R.dimen.size_fab)
        val avatarPadding = resources.getDimensionPixelSize(R.dimen.spacing_double)
        return (avatarGrid?.width ?: 0) / (avatarSize + avatarPadding)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_SELECTED_AVATAR_INDEX, (avatarGrid?.checkedItemPosition ?: 0))
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstNameView = view.findViewById<EditText>(R.id.first_name)
        lastInitialView = view.findViewById<EditText>(R.id.last_initial)
        doneFab = view.findViewById<FloatingActionButton>(R.id.done)
        avatarGrid = view.findViewById<GridView>(R.id.avatars)

        if (edit || (player != null && player!!.valid())) {
            initContentViews()
            initContents()
        }
        hideEmptyView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun hideEmptyView() {
        view?.run {
            findViewById<View>(R.id.empty).visibility = View.GONE
            findViewById<View>(R.id.content).visibility = View.VISIBLE
        }
    }

    private fun initContentViews() {
        val textWatcher = object : TextWatcher by TextWatcherAdapter {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // hiding the floating action button if text is empty
                if (s.isEmpty()) {
                    doneFab?.hide()
                }
            }

            // showing the floating action button if avatar is selected and input data is valid
            override fun afterTextChanged(s: Editable) {
                if (isAvatarSelected() && isInputDataValid()) doneFab?.show()
            }
        }

        firstNameView?.addTextChangedListener(textWatcher)
        lastInitialView?.addTextChangedListener(textWatcher)
        doneFab?.setOnClickListener {
            if (it.id == R.id.done) {
                val first = firstNameView?.text?.toString()
                val last = lastInitialView?.text?.toString()
                activity?.run {
                    val toSave = player?.apply {
                        // either update the existing player object
                        firstName = first
                        lastInitial = last
                        avatar = selectedAvatar
                    } ?: Player(first, last, selectedAvatar) /* or create a new one */

                    login.savePlayer(this, toSave) {
                        Log.d(TAG, "Saving login info successful.")
                    }
                }
            }
            removeDoneFab {
                performSignInWithTransition(selectedAvatarView
                        ?: avatarGrid?.getChildAt(selectedAvatar!!.ordinal))
            }
        }
    }

    private fun removeDoneFab(endAction: () -> Unit) {
        ViewCompat.animate(doneFab)
                .scaleX(0f)
                .scaleY(0f)
                .setInterpolator(FastOutSlowInInterpolator())
                .withEndAction(endAction)
                .start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun performSignInWithTransition(v: View? = null) {
        if (v == null || ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            // Don't run a transition if the passed view is null
            activity?.run {
                navigateToCategoryActivity()
            }
            return
        }

        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            activity?.run {
                window.sharedElementExitTransition.addListener(object :
                        Transition.TransitionListener by TransitionListenerAdapter {
                    override fun onTransitionEnd(transition: Transition) {
                        finish()
                    }
                })

                val pairs = TransitionHelper.createSafeTransitionParticipants(this, true,
                        Pair(v, getString(R.string.transition_avatar)))
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
                ActivityLaunchHelper.launchCategorySelection(this, options)
            }
        }
    }

    private fun initContents() {
        player?.run {
            valid().let {
                firstNameView?.setText(firstName)
                lastInitialView?.setText(lastInitial)
                avatar?.run { selectAvatar(this) }
            }
        }
    }

    private fun isAvatarSelected() = selectedAvatarView != null || selectedAvatar != null

    private fun selectAvatar(avatar: Avatar) {
        selectedAvatar = avatar
        avatarGrid?.run {
            requestFocusFromTouch()
            setItemChecked(avatar.ordinal, true)
        }
        showFab()
    }

    private fun showFab() {
        if (isInputDataValid()) doneFab?.show()
    }

    private fun isInputDataValid() =
            firstNameView?.text?.isNotEmpty() == true &&
                    lastInitialView?.text?.isNotEmpty() == true &&
                    selectedAvatar != null

    companion object {

        private const val ARG_EDIT = "EDIT"
        private const val KEY_SELECTED_AVATAR_INDEX = "selectedAvatarIndex"

        fun newInstance(edit: Boolean = false): SignInFragment {
            return SignInFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_EDIT, edit)
                }
            }
        }
    }
}
