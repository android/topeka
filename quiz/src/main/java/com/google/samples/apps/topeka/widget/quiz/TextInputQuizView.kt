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

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.Quiz
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.widget.TextWatcherAdapter

abstract class TextInputQuizView<out Q : Quiz<*>>(
        context: Context,
        category: Category,
        quiz: Q
) : AbsQuizView<Q>(context, category, quiz), TextView.OnEditorActionListener {

    protected fun createEditText(): EditText {
        return inflate<EditText>(R.layout.quiz_edit_text)
                .apply {
                    addTextChangedListener(object : TextWatcher by TextWatcherAdapter {
                        override fun afterTextChanged(text: Editable) =
                                allowAnswer(text.isNotEmpty())
                    })
                    setOnEditorActionListener(this@TextInputQuizView)
                }
    }

    override fun submitAnswer() {
        hideKeyboard(this)
        super.submitAnswer()
    }

    /**
     * Convenience method to hide the keyboard.

     * @param view A view in the hierarchy.
     */
    protected fun hideKeyboard(view: View?) {
        if (view != null) inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val inputMethodManager
        get() = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        v?.text?.isNotEmpty().let {
            allowAnswer()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitAnswer()
                hideKeyboard(v)
                return true
            }
        }
        return false
    }
}
