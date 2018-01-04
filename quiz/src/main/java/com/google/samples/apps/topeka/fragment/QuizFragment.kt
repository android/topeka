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
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterViewAnimator
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.samples.apps.topeka.adapter.QuizAdapter
import com.google.samples.apps.topeka.adapter.ScoreAdapter
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.helper.database
import com.google.samples.apps.topeka.helper.inflate
import com.google.samples.apps.topeka.helper.onLayoutChange
import com.google.samples.apps.topeka.helper.requestLogin
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.widget.AvatarView
import com.google.samples.apps.topeka.widget.quiz.AbsQuizView

/**
 * Encapsulates Quiz solving and displays it to the user.
 */
class QuizFragment : Fragment() {

    private val category by lazy(LazyThreadSafetyMode.NONE) {
        val categoryId = arguments!!.getString(Category.TAG)
        activity!!.database().getCategoryWith(categoryId)
    }

    private val quizAdapter by lazy(LazyThreadSafetyMode.NONE) {
        context?.run {
            QuizAdapter(this, category)
        }
    }

    val scoreAdapter by lazy(LazyThreadSafetyMode.NONE) {
        context?.run {
            ScoreAdapter(this, category)
        }
    }

    val quizView by lazy(LazyThreadSafetyMode.NONE) {
        view?.findViewById<AdapterViewAnimator>(R.id.quiz_view)
    }

    private val progressText by lazy(LazyThreadSafetyMode.NONE) {
        view?.findViewById<TextView>(R.id.progress_text)
    }

    private val progressBar by lazy(LazyThreadSafetyMode.NONE) {
        (view?.findViewById<ProgressBar>(R.id.progress))?.apply {
            max = category.quizzes.size
        }
    }
    private var solvedStateListener: SolvedStateListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Create a themed Context and custom LayoutInflater
        // to get custom themed views in this Fragment.
        val context = ContextThemeWrapper(activity, category.theme.styleId)
        return context.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setProgress(category.firstUnsolvedQuizPosition)
        decideOnViewToDisplay()
        setQuizViewAnimations()
        setAvatarDrawable()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun decideOnViewToDisplay() {
        if (category.solved) {
            // display the summary
            view?.let {
                with(it.findViewById<ListView>(R.id.scorecard)) {
                    adapter = scoreAdapter
                    visibility = View.VISIBLE
                }
                quizView?.visibility = View.GONE
            }
            solvedStateListener?.onCategorySolved()
        } else {
            with(quizView ?: return) {
                adapter = quizAdapter
                setSelection(category.firstUnsolvedQuizPosition)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setQuizViewAnimations() {
        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            return
        }
        with(quizView ?: return) {
            setInAnimation(activity, R.animator.slide_in_bottom)
            setOutAnimation(activity, R.animator.slide_out_top)
        }
    }

    private fun setAvatarDrawable(avatarView: AvatarView? = view?.findViewById(R.id.avatar)) {
        activity?.requestLogin { player ->
                if (player.valid()) {
                    avatarView?.setAvatar(player.avatar!!.drawableId)
                    with(ViewCompat.animate(avatarView)) {
                        interpolator = FastOutLinearInInterpolator()
                        startDelay = 500
                        scaleX(1f)
                        scaleY(1f)
                        start()
                    }
                }
            }
        }

    private fun setProgress(currentQuizPosition: Int) {
        if (isAdded) {
            progressText?.text = getString(
                    com.google.samples.apps.topeka.base.R.string.quiz_of_quizzes,
                    currentQuizPosition,
                    category.quizzes.size)
            progressBar?.progress = currentQuizPosition
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val focusedChild = quizView?.focusedChild
        if (focusedChild is ViewGroup) {
            val currentView = focusedChild.getChildAt(0)
            if (currentView is AbsQuizView<*>) {
                outState.putBundle(KEY_USER_INPUT, currentView.userInput)
            }
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        restoreQuizState(savedInstanceState)
        super.onViewStateRestored(savedInstanceState)
    }

    private fun restoreQuizState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return
        quizView?.onLayoutChange {
            val currentChild = (this as ViewGroup).getChildAt(0)
            if (currentChild is ViewGroup) {
                val potentialQuizView = currentChild.getChildAt(0)
                if (potentialQuizView is AbsQuizView<*>) {
                    potentialQuizView.userInput = savedInstanceState.getBundle(KEY_USER_INPUT)
                            ?: Bundle.EMPTY
                }
            }
        }

    }

    /**
     * Displays the next page.

     * @return `true` if there's another quiz to solve, else `false`.
     */
    fun showNextPage(): Boolean {
        quizView?.let {
            val nextItem = it.displayedChild + 1
            setProgress(nextItem)
            if (nextItem < it.adapter.count) {
                it.showNext()
                activity?.database()?.updateCategory(category)
                return true
            }
        }
        markCategorySolved()
        return false
    }

    private fun markCategorySolved() {
        category.solved = true
        activity?.database()?.updateCategory(category)
    }

    fun hasSolvedStateListener() = solvedStateListener != null

    fun setSolvedStateListener(solvedStateListener: SolvedStateListener) {
        this.solvedStateListener = solvedStateListener
        if (category.solved) solvedStateListener.onCategorySolved()
    }

    /**
     * Interface definition for a callback to be invoked when the quiz is started.
     */
    interface SolvedStateListener {

        /**
         * This method will be invoked when the category has been solved.
         */
        fun onCategorySolved()
    }

    companion object {

        private const val KEY_USER_INPUT = "USER_INPUT"

        fun newInstance(categoryId: String,
                        listener: SolvedStateListener?
        ): QuizFragment {
            return QuizFragment().apply {
                solvedStateListener = listener
                arguments = Bundle().apply { putString(Category.TAG, categoryId) }
            }
        }
    }
}