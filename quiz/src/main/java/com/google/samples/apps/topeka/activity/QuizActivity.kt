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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.annotation.VisibleForTesting
import android.support.design.widget.FloatingActionButton
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.android.instantapps.InstantApps
import com.google.samples.apps.topeka.fragment.QuizFragment
import com.google.samples.apps.topeka.helper.ActivityLaunchHelper
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.helper.FOREGROUND_COLOR
import com.google.samples.apps.topeka.helper.database
import com.google.samples.apps.topeka.helper.findFragmentById
import com.google.samples.apps.topeka.helper.findFragmentByTag
import com.google.samples.apps.topeka.helper.isLoggedIn
import com.google.samples.apps.topeka.helper.onSmartLockResult
import com.google.samples.apps.topeka.helper.requestLogin
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.JsonAttributes
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.widget.TextSharedElementCallback
import com.google.samples.apps.topeka.base.R as RBase

@get:VisibleForTesting
val countingIdlingResource = CountingIdlingResource("Quiz")

class QuizActivity : AppCompatActivity() {

    private val interpolator: Interpolator = FastOutSlowInInterpolator()
    private lateinit var category: Category
    private var quizFab: FloatingActionButton? = null
    private var icon: ImageView? = null
    private var toolbarBack: View? = null

    private lateinit var circularReveal: Animator
    private lateinit var colorChange: ObjectAnimator
    private var savedStateIsPlaying = false
    private var quizFragment: QuizFragment? = null

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.fab_quiz -> startQuizFromClickOn(it)
            RBase.id.submitAnswer -> submitAnswer()
            RBase.id.quiz_done -> ActivityCompat.finishAfterTransition(this@QuizActivity)
            R.id.back -> onBackPressed()
            else -> throw UnsupportedOperationException(
                    "OnClick has not been implemented for " + resources.getResourceName(it.id))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null != savedInstanceState) {
            savedStateIsPlaying = savedInstanceState.getBoolean(STATE_IS_PLAYING)
        }
        with(intent.data) {
            if (path.startsWith("/quiz")) {
                populate(lastPathSegment)
            } else {
                Log.w(FRAGMENT_TAG, "Path is invalid, finishing activity")
                ActivityLaunchHelper.launchCategorySelection(this@QuizActivity)
                supportFinishAfterTransition()
            }
        }
        val categoryNameTextSize = resources
                .getDimensionPixelSize(RBase.dimen.category_item_text_size)
        val paddingStart = resources.getDimensionPixelSize(RBase.dimen.spacing_double)
        val startDelay = resources.getInteger(RBase.integer.toolbar_transition_duration).toLong()
        ActivityCompat.setEnterSharedElementCallback(this,
                object : TextSharedElementCallback(categoryNameTextSize.toFloat(), paddingStart) {
                    override fun onSharedElementStart(sharedElementNames: List<String>?,
                                                      sharedElements: List<View>,
                                                      sharedElementSnapshots: List<View>?) {
                        super.onSharedElementStart(sharedElementNames,
                                sharedElements,
                                sharedElementSnapshots)
                        toolbarBack?.let {
                            it.scaleX = 0f
                            it.scaleY = 0f
                        }
                    }

                    override fun onSharedElementEnd(sharedElementNames: List<String>?,
                                                    sharedElements: List<View>,
                                                    sharedElementSnapshots: List<View>?) {
                        super.onSharedElementEnd(sharedElementNames,
                                sharedElements,
                                sharedElementSnapshots)
                        // Make sure to perform this animation after the transition has ended.
                        ViewCompat.animate(toolbarBack)
                                .setStartDelay(startDelay)
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                    }
                })

        if (!isLoggedIn()) requestLogin {}
    }

    @SuppressLint("NewApi")
    private fun populate(categoryId: String) {
        category = database().getCategoryWith(categoryId).also {
            setTheme(it.theme.styleId)
            if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
                window.statusBarColor = ContextCompat.getColor(this, it.theme.primaryDarkColor)
            }
            initLayout(it.id)
            initToolbar(it)
        }
    }

    private fun initLayout(categoryId: String) {
        setContentView(R.layout.activity_quiz)

        val packageName =
                if (InstantApps.isInstantApp(this))
                    "$packageName.quiz"
                else packageName

        icon = (findViewById<ImageView>(R.id.icon)).apply {
            val resId = resources.getIdentifier("$IMAGE_CATEGORY$categoryId",
                    "drawable",
                    packageName)

            setImageResource(resId)
            ViewCompat.animate(this)
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setStartDelay(300L)
                    .start()
        }
        quizFab = (findViewById<FloatingActionButton>(R.id.fab_quiz)).apply {
            setImageResource(RBase.drawable.ic_play)
            if (savedStateIsPlaying) hide() else show()
            setOnClickListener(onClickListener)
        }
        toolbarBack = findViewById<View>(R.id.back).apply { setOnClickListener(onClickListener) }
    }

    private fun initToolbar(category: Category) {

        with(findViewById<TextView>(R.id.category_title)) {
            text = category.name
            setTextColor(ContextCompat.getColor(this@QuizActivity, category.theme.textPrimaryColor))
        }
        if (savedStateIsPlaying) {
            // the toolbar should not have more elevation than the content while playing
            setToolbarElevation(false)
        }
    }

    override fun onResume() {
        if (savedStateIsPlaying) {
            with(findFragmentByTag(FRAGMENT_TAG) as QuizFragment) {
                if (!hasSolvedStateListener()) setSolvedStateListener(solvedStateListener)
            }
            findViewById<View>(R.id.quiz_fragment_container).visibility = View.VISIBLE
            quizFab?.hide()
            icon?.visibility = View.GONE
        } else {
            initQuizFragment()
        }
        super.onResume()
    }

    private fun initQuizFragment() {
        if (quizFragment != null) return
        quizFragment = QuizFragment.newInstance(category.id, solvedStateListener)
        // the toolbar should not have more elevation than the content while playing
        setToolbarElevation(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onSmartLockResult(requestCode, resultCode, data,
                success = {}, failure = { requestLogin {} })
        super.onActivityResult(requestCode, resultCode, data)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_IS_PLAYING, quizFab?.visibility == View.GONE)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {

        ViewCompat.animate(toolbarBack)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(100L)
                .start()

        // Scale the icon and fab to 0 size before calling onBackPressed if it exists.
        ViewCompat.animate(icon)
                .scaleX(.7f)
                .scaleY(.7f)
                .alpha(0f)
                .setInterpolator(interpolator)
                .start()

        ViewCompat.animate(quizFab)
                .scaleX(0f)
                .scaleY(0f)
                .setInterpolator(interpolator)
                .setStartDelay(100L)
                .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                    @SuppressLint("NewApi")
                    override fun onAnimationEnd(view: View?) {
                        if (isFinishing ||
                                ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1) &&
                                isDestroyed) return
                        super@QuizActivity.onBackPressed()
                    }
                })
                .start()
    }

    private fun startQuizFromClickOn(clickedView: View) {
        initQuizFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.quiz_fragment_container, quizFragment, FRAGMENT_TAG)
                .commit()
        val container = (findViewById<FrameLayout>(R.id.quiz_fragment_container)).apply {
            setBackgroundColor(ContextCompat.getColor(
                    this@QuizActivity,
                    category.theme.windowBackgroundColor))
        }
        revealFragmentContainer(clickedView, container)
        // the toolbar should not have more elevation than the content while playing
        setToolbarElevation(false)
    }

    @SuppressLint("NewApi")
    private fun revealFragmentContainer(clickedView: View,
                                        fragmentContainer: FrameLayout) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            revealFragmentContainerLollipop(clickedView, fragmentContainer)
        } else {
            fragmentContainer.visibility = View.VISIBLE
            clickedView.visibility = View.GONE
            icon?.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun revealFragmentContainerLollipop(clickedView: View,
                                                fragmentContainer: FrameLayout) {
        prepareCircularReveal(clickedView, fragmentContainer)

        ViewCompat.animate(clickedView)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setInterpolator(interpolator)
                .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                    override fun onAnimationEnd(view: View?) {
                        fragmentContainer.visibility = View.VISIBLE
                        clickedView.visibility = View.GONE
                    }
                })
                .start()

        fragmentContainer.visibility = View.VISIBLE
        with(AnimatorSet()) {
            play(circularReveal).with(colorChange)
            start()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun prepareCircularReveal(startView: View, targetView: FrameLayout) {
        val centerX = (startView.left + startView.right) / 2
        // Subtract the start view's height to adjust for relative coordinates on screen.
        val centerY = (startView.top + startView.bottom) / 2 - startView.height
        val endRadius = Math.hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
        circularReveal = ViewAnimationUtils.createCircularReveal(
                targetView, centerX, centerY, startView.width.toFloat(), endRadius)
                .apply {
                    interpolator = FastOutLinearInInterpolator()

                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            icon?.visibility = View.GONE
                            removeListener(this)
                        }
                    })
                }
        // Adding a color animation from the FAB's color to transparent creates a dissolve like
        // effect to the circular reveal.
        val accentColor = ContextCompat.getColor(this, category.theme.accentColor)
        colorChange = ObjectAnimator.ofInt(targetView,
                targetView.FOREGROUND_COLOR, accentColor, Color.TRANSPARENT)
                .apply {
                    setEvaluator(ArgbEvaluator())
                    interpolator = this@QuizActivity.interpolator
                }
    }

    @SuppressLint("NewApi")
    fun setToolbarElevation(shouldElevate: Boolean) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            toolbarBack?.elevation = if (shouldElevate)
                resources.getDimension(RBase.dimen.elevation_header)
            else
                0f
        }
    }

    private fun setResultSolved() {
        setResult(RBase.id.solved, Intent().apply { putExtra(JsonAttributes.ID, category.id) })
    }

    private val solvedStateListener
        get() = object : QuizFragment.SolvedStateListener {
            override fun onCategorySolved() {
                setResultSolved()
                setToolbarElevation(true)
                displayDoneFab()
            }

            /*
            * We're re-using the already existing fab and give it some
            * new values. This has to run delayed due to the queued animation
            * to hide the fab initially.
            */
            private fun displayDoneFab() {
                with(circularReveal) {
                    if (isRunning) {
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                showQuizFabWithDoneIcon()
                                removeListener(this)
                            }
                        })
                    } else {
                        showQuizFabWithDoneIcon()
                    }
                }
            }

            private fun showQuizFabWithDoneIcon() {
                with(quizFab ?: return) {
                    setImageResource(RBase.drawable.ic_tick)
                    id = RBase.id.quiz_done
                    visibility = View.VISIBLE
                    scaleX = 0f
                    scaleY = 0f
                }
                ViewCompat.animate(quizFab)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(interpolator)
                        .setListener(null)
                        .start()
            }
        }

    private fun submitAnswer() {
        if (!countingIdlingResource.isIdleNow) countingIdlingResource.decrement()
        if (quizFragment == null) {
            quizFragment = findFragmentById(R.id.quiz_fragment_container) as QuizFragment
        }
        with(quizFragment ?: return) {
            if (!showNextPage()) {
                view?.let {
                    with(it.findViewById<ListView>(R.id.scorecard)) {
                        adapter = scoreAdapter
                        visibility = View.VISIBLE
                    }
                    quizView?.visibility = View.GONE
                }
                setResultSolved()
                return
            }
        }
    }

    /**
     * Proceeds the quiz to it's next state.
     */
    fun proceed() = submitAnswer()

    /**
     * Solely exists for testing purposes and makes sure Espresso does not get confused.
     */
    fun lockIdlingResource() = countingIdlingResource.increment()

    companion object {

        private val IMAGE_CATEGORY = "image_category_"
        private val STATE_IS_PLAYING = "isPlaying"
        private val FRAGMENT_TAG = "Quiz"
    }
}
