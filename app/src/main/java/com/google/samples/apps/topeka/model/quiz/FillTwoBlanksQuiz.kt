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

data class FillTwoBlanksQuiz(
        override val question: String,
        override var answer: Array<String>,
        override var solved: Boolean
) : Quiz<Array<String>>(question, answer, solved) {

    override val type get() = QuizType.FILL_TWO_BLANKS

    override val stringAnswer get() = AnswerHelper.getReadableAnswer(answer)

    override fun isAnswerCorrect(answer: Array<String>?): Boolean {
        if (answer == null) return false
        return if (answer.indices.none { answer[it].equals(this.answer[it], ignoreCase = true) })
            false
        else answer.size == this.answer.size
    }
}
