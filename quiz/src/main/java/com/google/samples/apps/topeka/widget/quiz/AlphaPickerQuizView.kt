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
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz
import com.google.samples.apps.topeka.widget.SeekBarListener
import java.util.Arrays

@SuppressLint("ViewConstructor")
class AlphaPickerQuizView(
        context: Context,
        category: Category,
        quiz: AlphaPickerQuiz
) : AbsQuizView<AlphaPickerQuiz>(context, category, quiz) {

    private val alphabet get() = Arrays.asList(*resources.getStringArray(R.array.alphabet))

    private val KEY_SELECTION = "SELECTION"

    private var currentSelection: TextView? = null

    private var seekBar: SeekBar? = null

    override fun createQuizContentView(): View {
        val layout = inflate<ScrollView>(R.layout.quiz_layout_picker)
        currentSelection = (layout.findViewById<TextView>(R.id.seekbar_progress)).apply {
            text = alphabet[0]
        }
        seekBar = (layout.findViewById<SeekBar>(R.id.seekbar)).apply {
            max = alphabet.size - 1
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener by
            SeekBarListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    currentSelection?.text = alphabet[progress]
                    allowAnswer()
                }
            })
        }
        return layout
    }

    override val isAnswerCorrect get() = quiz
            .isAnswerCorrect(currentSelection?.text?.toString() ?: "")

    override var userInput: Bundle
        get() = Bundle().apply { putString(KEY_SELECTION, currentSelection?.text?.toString()) }
        set(savedInput) {
            seekBar?.progress = alphabet.indexOf(savedInput.getString(KEY_SELECTION, alphabet[0]))
        }
}
