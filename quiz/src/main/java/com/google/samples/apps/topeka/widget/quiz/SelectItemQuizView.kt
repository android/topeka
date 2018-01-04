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
import android.widget.ListView

import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.adapter.OptionsQuizAdapter
import com.google.samples.apps.topeka.helper.AnswerHelper
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.SelectItemQuiz

@SuppressLint("ViewConstructor")
class SelectItemQuizView(
        context: Context,
        category: Category,
        quiz: SelectItemQuiz
) : AbsQuizView<SelectItemQuiz>(context, category, quiz) {

    private val KEY_ANSWERS = "ANSWERS"
    private var answers = BooleanArray(quiz.options.size)

    override fun createQuizContentView(): View? {
        return ListView(context).apply {
            divider = null
            setSelector(com.google.samples.apps.topeka.base.R.drawable.selector_button)
            adapter = OptionsQuizAdapter(quiz.options, R.layout.item_answer_start,
                    context, true)
            choiceMode = AbsListView.CHOICE_MODE_SINGLE
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                allowAnswer()
                toggleAnswerFor(position)
            }
        }
    }

    override val isAnswerCorrect: Boolean
        get() {
            return AnswerHelper.isAnswerCorrect(
                    (quizContentView as ListView).checkedItemPositions,
                    quiz.answer)
        }

    override var userInput: Bundle
        get() = Bundle().apply { putBooleanArray(KEY_ANSWERS, answers) }
        set(savedInput) {
            savedInput.getBooleanArray(KEY_ANSWERS)?.let {
                answers = it
                val quizContentView = quizContentView as ListView
                val adapter = quizContentView.adapter
                answers.indices.forEach {
                    quizContentView.performItemClick(
                            quizContentView.getChildAt(it), it, adapter.getItemId(it))
                }
            }
        }

    private fun toggleAnswerFor(answerId: Int) {
        answers[answerId] = !answers[answerId]
    }
}
