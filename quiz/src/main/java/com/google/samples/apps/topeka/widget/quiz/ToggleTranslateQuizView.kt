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
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.Checkable
import android.widget.ListView
import com.google.samples.apps.topeka.base.R

import com.google.samples.apps.topeka.adapter.OptionsQuizAdapter
import com.google.samples.apps.topeka.helper.AnswerHelper
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz

@SuppressLint("ViewConstructor")
class ToggleTranslateQuizView(
        context: Context,
        category: Category,
        quiz: ToggleTranslateQuiz
) : AbsQuizView<ToggleTranslateQuiz>(context, category, quiz) {

    private var answers = BooleanArray(quiz.options.size)
    private var listView: ListView? = null

    override fun createQuizContentView(): View {
        return ListView(context).apply {
            listView = this
            divider = null
            setSelector(R.drawable.selector_button)
            adapter = OptionsQuizAdapter(quiz.readableOptions,
                    android.R.layout.simple_list_item_multiple_choice)
            choiceMode = AbsListView.CHOICE_MODE_MULTIPLE
            onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
                toggleAnswerFor(position)
                if (view is Checkable) {
                    view.isChecked = answers[position]
                }
                allowAnswer()
            }
        }
    }

    override val isAnswerCorrect: Boolean
        get() {
            val checkedItemPositions = listView?.checkedItemPositions
            val answer = quiz.answer
            return AnswerHelper.isAnswerCorrect(checkedItemPositions ?: return false, answer)
        }

    override var userInput: Bundle
        get() = Bundle().apply { putBooleanArray(KEY_ANSWER, answers) }
        set(savedInput) {
            savedInput.getBooleanArray(KEY_ANSWER)?.let {
                answers = it
            }
            listView?.let {
                for (i in answers.indices) {
                    it.performItemClick(it.getChildAt(i), i, it.adapter.getItemId(i))
                }
            }
        }

    private fun toggleAnswerFor(answerId: Int) {
        answers[answerId] = !answers[answerId]
    }
}
