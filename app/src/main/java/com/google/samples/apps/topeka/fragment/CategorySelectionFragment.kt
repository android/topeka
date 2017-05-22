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

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

import com.google.samples.apps.topeka.R
import com.google.samples.apps.topeka.activity.QuizActivity
import com.google.samples.apps.topeka.adapter.CategoryAdapter
import com.google.samples.apps.topeka.helper.TransitionHelper
import com.google.samples.apps.topeka.helper.beforeDrawing
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.JsonAttributes
import com.google.samples.apps.topeka.widget.OffsetDecoration

class CategorySelectionFragment : Fragment() {

    private val adapter: CategoryAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        CategoryAdapter(activity,
                AdapterView.OnItemClickListener { _, v, position, _ ->
                    adapter?.getItem(position)?.let {
                        startQuizActivityWithTransition(activity,
                                v.findViewById(R.id.category_title), it)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpQuizGrid(view.findViewById<RecyclerView>(R.id.categories))
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NewApi")
    private fun setUpQuizGrid(categoriesView: RecyclerView) {
        with(categoriesView) {
            addItemDecoration(OffsetDecoration(context.resources
                    .getDimensionPixelSize(R.dimen.spacing_nano)))
            adapter = this@CategorySelectionFragment.adapter
            beforeDrawing { activity.supportStartPostponedEnterTransition() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CATEGORY && resultCode == R.id.solved) {
            adapter?.notifyItemChanged(data.getStringExtra(JsonAttributes.ID))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startQuizActivityWithTransition(activity: Activity, toolbar: View,
                                                category: Category) {

        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                *TransitionHelper.createSafeTransitionParticipants(activity,
                        false,
                        Pair(toolbar, activity.getString(R.string.transition_toolbar))))
                .toBundle()

        // Start the activity with the participants, animating from one to the other.
        val startIntent = QuizActivity.getStartIntent(activity, category)
        ActivityCompat.startActivityForResult(activity,
                startIntent,
                REQUEST_CATEGORY,
                animationBundle)
    }

    companion object {

        private const val REQUEST_CATEGORY = 0x2300
    }

}
