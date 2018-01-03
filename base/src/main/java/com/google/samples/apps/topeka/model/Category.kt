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

package com.google.samples.apps.topeka.model

import android.util.Log
import com.google.samples.apps.topeka.model.quiz.Quiz

data class Category(
        val name: String,
        val id: String,
        val theme: Theme,
        val quizzes: List<Quiz<*>>,
        val scores: IntArray,
        var solved: Boolean) {

    init {
        if (quizzes.size != scores.size) {
            throw IllegalArgumentException(
                    "Expected ${quizzes.size} and ${scores.size} to be of the same length")
        }
    }

    /**
     * Updates a score for a provided quiz within this category.

     * @param which The quiz to rate.
     *
     * @param correctlySolved `true` if the quiz was solved else `false`.
     */
    fun setScore(which: Quiz<*>, correctlySolved: Boolean) {
        val index = quizzes.indexOf(which)
        Log.d(TAG, "Setting score for $which with index $index")
        if (-1 == index) {
            return
        }
        scores[index] = if (correctlySolved) SCORE else NO_SCORE
    }

    fun isSolvedCorrectly(quiz: Quiz<*>) = getScore(quiz) == SCORE

    /**
     * Gets the score for a single quiz.

     * @param which The quiz to look for
     *
     * @return The score if found, else 0.
     */
    fun getScore(which: Quiz<*>): Int {
        try {
            return scores[quizzes.indexOf(which)]
        } catch (ioobe: IndexOutOfBoundsException) {
            return 0
        }

    }

    /**
     * @return The sum of all quiz scores within this category.
     */
    val score: Int get() = scores.sum()

    /**
     * Checks which quiz is the first unsolved within this category.

     * @return The position of the first unsolved quiz.
     */
    val firstUnsolvedQuizPosition
        get() = quizzes.indices.firstOrNull { !quizzes[it].solved } ?: quizzes.size


    override fun toString() = "Category{name='$name', id='$id', theme=$theme, quizzes=$quizzes, " +
            "scores=$scores, solved=$solved}"

    companion object {

        const val TAG = "Category"
        private const val SCORE = 8
        private const val NO_SCORE = 0
    }

}
