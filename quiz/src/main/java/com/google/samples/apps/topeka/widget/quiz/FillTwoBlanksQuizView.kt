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
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout

import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz

@SuppressLint("ViewConstructor")
class FillTwoBlanksQuizView(
        context: Context,
        category: Category,
        quiz: FillTwoBlanksQuiz
) : TextInputQuizView<FillTwoBlanksQuiz>(context, category, quiz) {

    private val KEY_ANSWER_ONE = "ANSWER_ONE"
    private val KEY_ANSWER_TWO = "ANSWER_TWO"

    private var answerOne: EditText? = null
    private var answerTwo: EditText? = null

    override fun createQuizContentView(): View {
        val layout = LinearLayout(context)
        answerOne = createEditText().apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
        }
        answerTwo = createEditText().apply {
            id = com.google.samples.apps.topeka.base.R.id.quiz_edit_text_two
        }
        layout.orientation = LinearLayout.VERTICAL
        addEditText(layout, answerOne)
        addEditText(layout, answerTwo)
        return layout
    }

    override var userInput: Bundle
        get() {
            return Bundle().apply {
                putString(KEY_ANSWER_ONE, answerOne?.text?.toString())
                putString(KEY_ANSWER_TWO, answerTwo?.text?.toString())
            }
        }
        set(savedInput) {
            answerOne?.setText(savedInput.getString(KEY_ANSWER_ONE))
            answerTwo?.setText(savedInput.getString(KEY_ANSWER_TWO))
        }

    private fun addEditText(layout: LinearLayout, editText: EditText?) {
        layout.addView(editText, LinearLayout
                .LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0, 1f))
    }

    override val isAnswerCorrect: Boolean
        get() {
            val partOne = getAnswerFrom(answerOne)
            val partTwo = getAnswerFrom(answerTwo)
            return quiz.isAnswerCorrect(arrayOf(partOne, partTwo))
        }

    private fun getAnswerFrom(view: EditText?) = view?.text.toString()
}
