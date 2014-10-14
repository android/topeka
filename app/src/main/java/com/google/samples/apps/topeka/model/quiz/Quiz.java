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
import com.google.samples.apps.topeka.model.JsonAttributes;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Quiz<A> implements Parcelable {

    public enum Type {
        ALPHA_PICKER,
        BOOLEAN,
        FILL_BLANK,
        FOUR_QUARTER,
        MULTI_SELECT,
        PICKER,
        SINGLE_SELECT_ITEM,
        TOGGLE_TRANSLATE
    }

    @SerializedName(JsonAttributes.QUESTION)
    private String mQuestion;

    @SerializedName(JsonAttributes.ANSWER)
    private A mAnswer;

    protected Quiz(String question, A answer) {
        mQuestion = question;
        mAnswer = answer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public A getAnswer() {
        return mAnswer;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject quizJSON = new JSONObject();
        quizJSON.put(JsonAttributes.QUESTION, mQuestion);
        quizJSON.put(JsonAttributes.ANSWER, mAnswer);
        return quizJSON;
    }

    protected Quiz(Parcel in) {
        mQuestion = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuestion);
    }
}
