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

/**
 * This abstract class provides general structure for quizzes.

 * @see com.google.samples.apps.topeka.model.quiz.QuizType

 * @see com.google.samples.apps.topeka.widget.quiz.AbsQuizView
 */
abstract class Quiz<A> protected constructor(
        open val question: String,
        open val answer: A,
        open var solved: Boolean) {

    private val quizType by lazy { type.jsonName }

    /**
     * @return The [QuizType] that represents this quiz.
     */
    abstract val type: QuizType

    /**
     * Implementations need to return a human readable version of the given answer.
     */
    abstract val stringAnswer: String

    open fun isAnswerCorrect(answer: A?) = this.answer == answer

    /**
     * @return The id of this quiz.
     */
    val id get() = question.hashCode()

    override fun toString() = """$type : " $question""""
}
