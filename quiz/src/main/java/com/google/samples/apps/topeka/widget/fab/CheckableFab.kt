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

package com.google.samples.apps.topeka.widget.fab

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable

import com.google.samples.apps.topeka.quiz.R

/**
 * A [FloatingActionButton] that implements [Checkable] to allow display of different
 * icons in it's states.
 */
class CheckableFab(
        context: Context,
        attrs: AttributeSet? = null
) : FloatingActionButton(context, attrs), Checkable {

    private var _checked = true

    init {
        setImageResource(R.drawable.answer_quiz_fab)
    }

    private val attrs = intArrayOf(android.R.attr.state_checked)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(1 + extraSpace)
        if (_checked) {
            View.mergeDrawableStates(drawableState, attrs)
        }
        return drawableState
    }

    override fun setChecked(checked: Boolean) {
        if (_checked == checked) return
        _checked = checked
        refreshDrawableState()
    }

    override fun isChecked() = _checked

    override fun toggle() {
        _checked = !_checked
    }

}
