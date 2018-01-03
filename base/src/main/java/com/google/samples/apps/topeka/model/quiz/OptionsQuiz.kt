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

package com.google.samples.apps.topeka.model.quiz

import com.google.samples.apps.topeka.helper.AnswerHelper
import java.util.Arrays

/**
 * Base class holding details for quizzes with several potential answers.

 * @param <T> The options that can result in an answer.
 */
abstract class OptionsQuiz<T>(
        question: String,
        answer: IntArray,
        open var options: Array<T>,
        solved: Boolean
) : Quiz<IntArray>(question, answer, solved) {

    override fun isAnswerCorrect(answer: IntArray?) = Arrays.equals(this.answer, answer)

    /**
     * Converts an array of answers with options to a readable answer.

     * @param answers The actual answers
     *
     * @param options The options to display.
     *
     * @return The readable answer.
     */
    fun getAnswer(answers: IntArray, options: Array<String>): String {
        return AnswerHelper.getReadableAnswer(Array(answers.size) { options[answers[it]] })
    }
}
