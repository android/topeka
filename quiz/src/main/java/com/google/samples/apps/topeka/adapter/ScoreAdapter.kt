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

package com.google.samples.apps.topeka.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.helper.inflate
import com.google.samples.apps.topeka.model.Category

/**
 * Adapter for displaying score cards.
 */
class ScoreAdapter(private val context: Context, private val category: Category) : BaseAdapter() {

    private val quizList = category.quizzes
    private val count = quizList.size
    private val successIcon by lazy {
        loadAndTint(
            com.google.samples.apps.topeka.base.R.drawable.ic_tick,
            com.google.samples.apps.topeka.base.R.color.theme_green_primary)
    }
    private val failedIcon by lazy {
        loadAndTint(com.google.samples.apps.topeka.base.R.drawable.ic_cross,
                com.google.samples.apps.topeka.base.R.color.theme_red_primary)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
            (convertView ?: createView(parent))
                    .apply {
                        with(tag as ViewHolder) {
                            with(getItem(position)) {
                                quizView.text = question
                                answerView.text = stringAnswer
                            }
                            setSolvedStateForQuiz(solvedState, position)
                        }
                    }

    private fun setSolvedStateForQuiz(solvedState: ImageView, position: Int) {
        val tintedImage = if (category.isSolvedCorrectly(getItem(position))) successIcon
        else failedIcon
        solvedState.setImageDrawable(tintedImage)
    }

    override fun getCount() = count

    override fun getItem(position: Int) = quizList[position]

    override fun getItemId(position: Int): Long {
        val itemId = if (position in 0..count + 1) position else AbsListView.INVALID_POSITION
        return itemId.toLong()
    }

    /**
     * Convenience method to aid tint of vector drawables at runtime.

     * @param drawableId The id of the drawable to load.
     *
     * @param tintColor The tint to apply.
     *
     * @return The tinted drawable.
     */
    private fun loadAndTint(@DrawableRes drawableId: Int, @ColorRes tintColor: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, drawableId)!!
        return DrawableCompat.wrap(drawable)
                .apply { DrawableCompat.setTint(this, ContextCompat.getColor(context, tintColor)) }
    }

    private fun createView(parent: ViewGroup) =
            (parent.context.inflate(R.layout.item_scorecard, parent, false) as ViewGroup)
                    .apply { tag = ViewHolder(this) }

    private inner class ViewHolder(scorecardItem: ViewGroup) {

        internal val answerView = scorecardItem.findViewById<TextView>(R.id.answer)
        internal val quizView = scorecardItem.findViewById<TextView>(R.id.quiz)
        internal val solvedState = scorecardItem.findViewById<ImageView>(R.id.solved_state)

    }
}
