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

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.google.samples.apps.topeka.model.JsonAttributes;
import com.google.samples.apps.topeka.model.quiz.abstracts.Quiz;

import org.json.JSONException;
import org.json.JSONObject;

public final class PickerQuiz extends Quiz<Integer> {

    @SerializedName(JsonAttributes.MIN)
    private final int mMin;

    @SerializedName(JsonAttributes.MAX)
    private final int mMax;

    @SerializedName(JsonAttributes.STEP)
    private final int mStep;

    public PickerQuiz(String question, Integer answer, int min, int max, int step) {
        super(question, answer);
        mMin = min;
        mMax = max;
        mStep = step;
    }

    protected PickerQuiz(Parcel in) {
        super(in);
        mMin = in.readInt();
        mMax = in.readInt();
        mStep = in.readInt();
    }

    public int getMin() {
        return mMin;
    }

    public int getMax() {
        return mMax;
    }

    public int getStep() {
        return mStep;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mMin);
        dest.writeInt(mMax);
        dest.writeInt(mStep);
    }
}
