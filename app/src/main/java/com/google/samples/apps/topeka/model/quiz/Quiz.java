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

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.samples.apps.topeka.helper.ParcelableHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This abstract class provides general structure for quizzes.
 *
 * @see com.google.samples.apps.topeka.model.quiz.QuizType
 * @see com.google.samples.apps.topeka.widget.quiz.AbsQuizView
 */
public abstract class Quiz<A> implements Parcelable {

    private static final String TAG = "Quiz";
    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @SuppressWarnings("TryWithIdenticalCatches")
        @Override
        public Quiz createFromParcel(Parcel in) {
            int ordinal = in.readInt();
            QuizType type = QuizType.values()[ordinal];
            try {
                Constructor<? extends Quiz> constructor = type.getType()
                        .getConstructor(Parcel.class);
                return constructor.newInstance(in);
            } catch (InstantiationException e) {
                performLegacyCatch(e);
            } catch (IllegalAccessException e) {
                performLegacyCatch(e);
            } catch (InvocationTargetException e) {
                performLegacyCatch(e);
            } catch (NoSuchMethodException e) {
                performLegacyCatch(e);
            }
            throw new UnsupportedOperationException("Could not create Quiz");
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    private static void performLegacyCatch(Exception e) {
        Log.e(TAG, "createFromParcel ", e);
    }

    private final String mQuestion;
    private final String mQuizType;
    private A mAnswer;
    /**
     * Flag indicating whether this quiz has already been solved.
     * It does not give information whether the solution was correct or not.
     */
    private boolean mSolved;

    protected Quiz(String question, A answer, boolean solved) {
        mQuestion = question;
        mAnswer = answer;
        mQuizType = getType().getJsonName();
        mSolved = solved;
    }

    protected Quiz(Parcel in) {
        mQuestion = in.readString();
        mQuizType = getType().getJsonName();
        mSolved = ParcelableHelper.readBoolean(in);
    }

    /**
     * @return The {@link QuizType} that represents this quiz.
     */
    public abstract QuizType getType();

    /**
     * Implementations need to return a human readable version of the given answer.
     */
    public abstract String getStringAnswer();

    public String getQuestion() {
        return mQuestion;
    }

    public A getAnswer() {
        return mAnswer;
    }

    protected void setAnswer(A answer) {
        mAnswer = answer;
    }

    public boolean isAnswerCorrect(A answer) {
        return mAnswer.equals(answer);
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    /**
     * @return The id of this quiz.
     */
    public int getId() {
        return getQuestion().hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableHelper.writeEnumValue(dest, getType());
        dest.writeString(mQuestion);
        ParcelableHelper.writeBoolean(dest, mSolved);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }

        Quiz quiz = (Quiz) o;

        if (mSolved != quiz.mSolved) {
            return false;
        }
        if (!mAnswer.equals(quiz.mAnswer)) {
            return false;
        }
        if (!mQuestion.equals(quiz.mQuestion)) {
            return false;
        }
        if (!mQuizType.equals(quiz.mQuizType)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mQuestion.hashCode();
        result = 31 * result + mAnswer.hashCode();
        result = 31 * result + mQuizType.hashCode();
        result = 31 * result + (mSolved ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return getType() + ": \"" + getQuestion() + "\"";
    }
}
