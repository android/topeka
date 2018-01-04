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
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.widget.TextWatcherAdapter

@SuppressLint("ViewConstructor")
class FillBlankQuizView(
        context: Context,
        category: Category,
        quiz: FillBlankQuiz
) : TextInputQuizView<FillBlankQuiz>(context, category, quiz) {

    private val KEY_ANSWER = "ANSWER"
    private var answerView: EditText? = null

    override fun createQuizContentView(): View? {
        val start = quiz.start
        val end = quiz.end
        if (null != start || null != end) {
            return getStartEndView(start, end)
        }
        answerView = createEditText()
        return answerView
    }

    override var userInput: Bundle
        get() = Bundle().apply { putString(KEY_ANSWER, answerView?.text?.toString()) }
        set(savedInput) {
            answerView?.setText(savedInput.getString(KEY_ANSWER))
        }

    /**
     * Creates and returns views that display the start and end of a question.

     * @param start The content of the start view.
     *
     * @param end The content of the end view.
     *
     * @return The created views within an appropriate container.
     */
    private fun getStartEndView(start: String?, end: String?): View {
        return inflate<View>(R.layout.quiz_fill_blank_with_surroundings)
                .apply {
                    answerView = (findViewById<EditText>(R.id.quiz_edit_text)).apply {
                        addTextChangedListener(object: TextWatcher by TextWatcherAdapter {
                            override fun afterTextChanged(p0: Editable?) {
                                allowAnswer(text.isNotEmpty())
                            }
                        })
                        setOnEditorActionListener(this@FillBlankQuizView)
                    }
                    setExistingContentOrHide(findViewById<TextView>(R.id.start), start)
                    setExistingContentOrHide(findViewById<TextView>(R.id.end), end)
                }
    }

    /**
     * Sets content to a [TextView]. If content is null, the view will not be displayed.

     * @param view The view to hold the text.
     *
     * @param content The text to display.
     */
    private fun setExistingContentOrHide(view: TextView, content: String?) {
        with(view) { if (content == null) visibility = View.GONE else text = content }
    }

    override val isAnswerCorrect get() = quiz.isAnswerCorrect(answerView?.text?.toString())
}
