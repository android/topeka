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

package com.google.samples.apps.topeka.model.quiz.abstracts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.google.samples.apps.topeka.model.JsonAttributes;

public abstract class OptionsQuiz<T> extends Quiz<int[]> {

    @SerializedName(JsonAttributes.OPTIONS)
    private T[] mOptions;

    public OptionsQuiz(String question, int[] answer, T[] options) {
        super(question, answer);
        mOptions = options;
    }

    public T[] getOptions() {
        return mOptions;
    }

    protected void setOptions(T[] options) {
        mOptions = options;
    }

    protected OptionsQuiz(Parcel in) {
        super(in);
        final int answerLength = in.readInt();
        final int answer[] = new int[answerLength];
        in.readIntArray(answer);
        setAnswer(answer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getAnswer().length);
        dest.writeIntArray(getAnswer());
    }
}
