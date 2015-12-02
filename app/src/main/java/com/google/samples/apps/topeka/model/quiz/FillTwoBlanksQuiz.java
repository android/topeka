/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.topeka.model.quiz;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.samples.apps.topeka.helper.AnswerHelper;

import java.util.Arrays;

@SuppressLint("ParcelCreator")
public final class FillTwoBlanksQuiz extends Quiz<String[]> {

    public FillTwoBlanksQuiz(@NonNull String question, @NonNull String[] answer, boolean solved) {
        super(question, answer, solved);
    }

    @SuppressWarnings("unused")
    public FillTwoBlanksQuiz(Parcel in) {
        super(in);
        String answer[] = in.createStringArray();
        setAnswer(answer);
    }

    @Override
    public QuizType getType() {
        return QuizType.FILL_TWO_BLANKS;
    }

    @Override
    public String getStringAnswer() {
        return AnswerHelper.getAnswer(getAnswer());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeStringArray(getAnswer());
    }

    @Override
    public boolean isAnswerCorrect(String[] answer) {
        String[] correctAnswers = getAnswer();
        if (answer == null || correctAnswers == null) {
            return false;
        }
        for (int i = 0; i < answer.length; i++) {
            if (!answer[i].equalsIgnoreCase(correctAnswers[i])) {
                return false;
            }
        }
        return answer.length == correctAnswers.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FillTwoBlanksQuiz)) {
            return false;
        }

        FillTwoBlanksQuiz quiz = (FillTwoBlanksQuiz) o;
        final String[] answer = getAnswer();
        final String question = getQuestion();
        if (answer != null ? !Arrays.equals(answer, quiz.getAnswer()) : quiz.getAnswer() != null) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (!question.equals(quiz.getQuestion())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(getAnswer());
        return result;
    }

}
