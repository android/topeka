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
import android.widget.SeekBar
import android.widget.TextView
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.PickerQuiz
import com.google.samples.apps.topeka.widget.SeekBarListener

@SuppressLint("ViewConstructor")
class PickerQuizView(
        context: Context,
        category: Category,
        quiz: PickerQuiz
) : AbsQuizView<PickerQuiz>(context, category, quiz) {

    private val KEY_ANSWER = "ANSWER"
    private var currentSelection: TextView? = null
    private var seekBar: SeekBar? = null
    private var step = 0
    private var min = quiz.min
    private var progress = 0


    override fun createQuizContentView(): View {
        initStep()
        min = quiz.min
        val layout = inflate<View>(R.layout.quiz_layout_picker)
        currentSelection = (layout.findViewById<TextView>(R.id.seekbar_progress)).apply {
            text = min.toString()
        }
        seekBar = (layout.findViewById<SeekBar>(R.id.seekbar)).apply {
            max = seekBarMax
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener by
            SeekBarListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    setCurrentSelectionText(this@PickerQuizView.min + progress)
                    allowAnswer()
                }
            })
        }
        return layout
    }

    private fun setCurrentSelectionText(progress: Int) {
        this.progress = progress / step * step
        currentSelection?.text = this.progress.toString()
    }

    override val isAnswerCorrect get() = quiz.isAnswerCorrect(progress)

    private fun initStep() {
        val tmpStep = quiz.step
        //make sure steps are never 0
        step = if (0 == tmpStep) 1 else tmpStep
    }

    override var userInput: Bundle
        get() = Bundle().apply { putInt(KEY_ANSWER, progress) }
        set(value) {
            seekBar?.progress = value.getInt(KEY_ANSWER) - min
        }

    /**
     * Calculates the actual max value of the SeekBar
     */
    private val seekBarMax: Int
        get() {
            val absMin = Math.abs(quiz.min)
            val absMax = Math.abs(quiz.max)
            val realMin = Math.min(absMin, absMax)
            val realMax = Math.max(absMin, absMax)
            return realMax - realMin
        }
}
