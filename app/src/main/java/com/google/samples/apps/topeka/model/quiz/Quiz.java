/*
 * Copyright 2014 Google Inc.
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

import com.google.gson.annotations.SerializedName;
import com.google.samples.apps.topeka.helper.ParcelableHelper;
import com.google.samples.apps.topeka.model.JsonAttributes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
        @Override
        public Quiz createFromParcel(Parcel in) {
            int ordinal = in.readInt();
            QuizType type = QuizType.values()[ordinal];
            try {
                Constructor<? extends Quiz> constructor = type.getType().getConstructor(
                        Parcel.class);
                return constructor.newInstance(in);
            } catch (InstantiationException e) {
                Log.e(TAG, "createFromParcel ", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "createFromParcel ", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "createFromParcel ", e);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "createFromParcel ", e);
            }
            throw new UnsupportedOperationException("Could not create Quiz");
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };
    @SerializedName(JsonAttributes.QUESTION)
    private final String mQuestion;
    @SerializedName(JsonAttributes.ANSWER)
    private A mAnswer;

    protected Quiz(String question, A answer) {
        mQuestion = question;
        mAnswer = answer;
    }

    protected Quiz(Parcel in) {
        mQuestion = in.readString();
    }

    public abstract QuizType getType();

    public String getQuestion() {
        return mQuestion;
    }

    public A getAnswer() {
        return mAnswer;
    }

    protected void setAnswer(A answer) {
        mAnswer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableHelper.writeEnumValue(dest, getType());
        dest.writeString(mQuestion);
    }

}
