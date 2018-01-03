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

package com.google.samples.apps.topeka.helper

import android.util.SparseBooleanArray

/**
 * Collection of methods to convert answers to human readable forms.
 */
object AnswerHelper {

    val SEPARATOR: String = System.getProperty("line.separator")

    /**
     * Converts an array of answers to a readable answer.

     * @param answers The answers to display.
     *
     * @return The readable answer.
     */
    fun getReadableAnswer(answers: Array<String>): String {
        val readableAnswer = StringBuilder()
        //Iterate over all answers
        answers.indices.forEach {
            val answer = answers[it]
            readableAnswer.append(answer)
            //Don't add a separator for the last answer
            if (it < answers.size - 1) {
                readableAnswer.append(SEPARATOR)
            }
        }
        return readableAnswer.toString()
    }

    /**
     * Checks whether a provided answer is correct.

     * @param checkedItems The items that were selected.
     *
     * @param answerIds The actual correct answer ids.
     *
     * @return `true` if correct else `false`.
     */
    fun isAnswerCorrect(checkedItems: SparseBooleanArray, answerIds: IntArray): Boolean {
        var checkedCount = 0
        for (i in 0 until answerIds.size) {
            if (!checkedItems.get(answerIds[i])) {
                return false
            }
            checkedCount++
        }
        return checkedCount == answerIds.size
    }

}