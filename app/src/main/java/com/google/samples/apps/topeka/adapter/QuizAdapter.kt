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

package com.google.samples.apps.topeka.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz
import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz
import com.google.samples.apps.topeka.model.quiz.FourQuarterQuiz
import com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz
import com.google.samples.apps.topeka.model.quiz.PickerQuiz
import com.google.samples.apps.topeka.model.quiz.Quiz
import com.google.samples.apps.topeka.model.quiz.QuizType
import com.google.samples.apps.topeka.model.quiz.SelectItemQuiz
import com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz
import com.google.samples.apps.topeka.widget.quiz.AbsQuizView
import com.google.samples.apps.topeka.widget.quiz.AlphaPickerQuizView
import com.google.samples.apps.topeka.widget.quiz.FillBlankQuizView
import com.google.samples.apps.topeka.widget.quiz.FillTwoBlanksQuizView
import com.google.samples.apps.topeka.widget.quiz.FourQuarterQuizView
import com.google.samples.apps.topeka.widget.quiz.MultiSelectQuizView
import com.google.samples.apps.topeka.widget.quiz.PickerQuizView
import com.google.samples.apps.topeka.widget.quiz.SelectItemQuizView
import com.google.samples.apps.topeka.widget.quiz.ToggleTranslateQuizView
import com.google.samples.apps.topeka.widget.quiz.TrueFalseQuizView

/**
 * Adapter to display quizzes.
 */
class QuizAdapter(private val context: Context, private val category: Category) : BaseAdapter() {

    private val quizzes = category.quizzes
    private val quizTypes = quizzes.indices.map { quizzes[it].type.jsonName }.toList()
    private val viewTypeCount = quizTypes.size

    override fun getCount() = quizzes.size

    override fun getItem(position: Int) = quizzes[position]

    override fun getItemId(position: Int) = quizzes[position].id.toLong()

    override fun getViewTypeCount() = viewTypeCount

    override fun getItemViewType(position: Int) = quizTypes.indexOf(getItem(position).type.jsonName)

    override fun hasStableIds() = true

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val quiz = getItem(position)
        return if (convertView is AbsQuizView<*> && convertView.quiz == quiz) convertView
        else createViewFor(quiz)
    }

    private fun createViewFor(quiz: Quiz<*>): AbsQuizView<Quiz<*>> {
        return when (quiz.type) {
            QuizType.ALPHA_PICKER -> AlphaPickerQuizView(context, category, quiz as AlphaPickerQuiz)
            QuizType.FILL_BLANK -> FillBlankQuizView(context, category, quiz as FillBlankQuiz)
            QuizType.FILL_TWO_BLANKS ->
                FillTwoBlanksQuizView(context, category, quiz as FillTwoBlanksQuiz)
            QuizType.FOUR_QUARTER -> FourQuarterQuizView(context, category, quiz as FourQuarterQuiz)
            QuizType.MULTI_SELECT -> MultiSelectQuizView(context, category, quiz as MultiSelectQuiz)
            QuizType.PICKER -> PickerQuizView(context, category, quiz as PickerQuiz)
            QuizType.SINGLE_SELECT, QuizType.SINGLE_SELECT_ITEM ->
                SelectItemQuizView(context, category, quiz as SelectItemQuiz)
            QuizType.TOGGLE_TRANSLATE ->
                ToggleTranslateQuizView(context, category, quiz as ToggleTranslateQuiz)
            QuizType.TRUE_FALSE -> TrueFalseQuizView(context, category, quiz as TrueFalseQuiz)
        }
    }
}
