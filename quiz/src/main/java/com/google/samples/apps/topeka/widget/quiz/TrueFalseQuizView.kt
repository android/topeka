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
import android.view.ViewGroup
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz
import com.google.samples.apps.topeka.quiz.R

@SuppressLint("ViewConstructor")
class TrueFalseQuizView(
        context: Context,
        category: Category,
        quiz: TrueFalseQuiz
) : AbsQuizView<TrueFalseQuiz>(context, category, quiz) {

    private var answer: Boolean = false
    private var answerTrue: View? = null
    private var answerFalse: View? = null

    override fun createQuizContentView(): View {
        val clickListener = View.OnClickListener {
            when (it.id) {
                R.id.answer_true -> answer = true
                R.id.answer_false -> answer = false
            }
            allowAnswer()
        }
        val container = inflate<ViewGroup>(R.layout.quiz_radio_group_true_false).also {
            answerTrue = it.findViewById(R.id.answer_true)
            answerFalse = it.findViewById(R.id.answer_false)
        }


        answerTrue?.setOnClickListener(clickListener)
        answerFalse?.setOnClickListener(clickListener)
        return container
    }

    override val isAnswerCorrect get() = quiz.isAnswerCorrect(answer)

    override var userInput: Bundle
        get() = Bundle().apply { putBoolean(KEY_ANSWER, answer) }
        set(value) {
            val tmpAnswer = value.getBoolean(KEY_ANSWER)
            performSelection(if (tmpAnswer) answerTrue ?: return else answerFalse ?: return)
        }

    private fun performSelection(selection: View) {
        selection.apply {
            performClick()
            isSelected = true
        }
    }
}
