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

import com.google.samples.apps.topeka.adapter.OptionsQuizAdapter
import com.google.samples.apps.topeka.helper.AnswerHelper
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz

@SuppressLint("ViewConstructor")
class MultiSelectQuizView(
        context: Context,
        category: Category,
        quiz: MultiSelectQuiz
) : AbsQuizView<MultiSelectQuiz>(context, category, quiz) {

    private var listView: ListView? = null

    override fun createQuizContentView(): View {
        return ListView(context).apply {
            listView = this
            adapter = OptionsQuizAdapter(quiz.options,
                    android.R.layout.simple_list_item_multiple_choice)
            choiceMode = AbsListView.CHOICE_MODE_MULTIPLE
            itemsCanFocus = false
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
                allowAnswer()
            }
        }
    }

    override val isAnswerCorrect: Boolean
        get() {
            val checkedItemPositions = listView?.checkedItemPositions
            val answer = quiz.answer
            return if (checkedItemPositions != null)
                AnswerHelper.isAnswerCorrect(checkedItemPositions, answer)
            else false
        }

    override var userInput: Bundle
        get() = Bundle().apply { putBooleanArray(KEY_ANSWER, bundleableAnswer) }
        set(savedInput) {
            val answers = savedInput.getBooleanArray(KEY_ANSWER) ?: return
            answers.indices.forEach { listView?.setItemChecked(it, answers[it]) }
        }

    private val bundleableAnswer: BooleanArray?
        get() {
            val checkedItemPositions = listView?.checkedItemPositions ?: return null
            val answerSize = checkedItemPositions.size()
            if (answerSize == 0) {
                return null
            }
            val optionsSize = quiz.options.size
            val bundleableAnswer = BooleanArray(optionsSize)
            (0 until answerSize).forEach {
                bundleableAnswer[checkedItemPositions.keyAt(it)] = checkedItemPositions.valueAt(it)
            }
            return bundleableAnswer
        }
}
