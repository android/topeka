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

package com.google.samples.apps.topeka.widget.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView

import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.adapter.OptionsQuizAdapter
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.helper.onLayoutChange
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.FourQuarterQuiz

@SuppressLint("ViewConstructor")
class FourQuarterQuizView(
        context: Context,
        category: Category,
        quiz: FourQuarterQuiz
) : AbsQuizView<FourQuarterQuiz>(context, category, quiz) {

    private var answeredPosition = -1
    private var answerView: GridView? = null

    override val isAnswerCorrect get() = quiz.isAnswerCorrect(intArrayOf(answeredPosition))

    override fun createQuizContentView(): View {
        return GridView(context).apply {
            answerView = this
            setSelector(com.google.samples.apps.topeka.base.R.drawable.selector_button)
            numColumns = 2
            quiz.options.let {
                adapter = OptionsQuizAdapter(quiz.options, R.layout.item_answer)
            }
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                allowAnswer()
                answeredPosition = position
            }
        }
    }

    override var userInput: Bundle
        get() = Bundle().apply { putInt(KEY_ANSWER, answeredPosition) }
        @SuppressLint("NewApi")
        set(savedInput) {
            answeredPosition = savedInput.getInt(KEY_ANSWER)
            if (answeredPosition != -1) {
                if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.KITKAT) && isLaidOut) {
                    setUpUserInput()
                } else {
                    onLayoutChange { setUpUserInput() }
                }
            }
        }

    private fun setUpUserInput() {
        with(answerView ?: return) {
            performItemClick(getChildAt(answeredPosition), answeredPosition,
                    adapter.getItemId(answeredPosition))
            getChildAt(answeredPosition)!!.isSelected = true
            setSelection(answeredPosition)
        }
    }

}
