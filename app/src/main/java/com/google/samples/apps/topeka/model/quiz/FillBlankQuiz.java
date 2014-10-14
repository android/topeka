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

public final class FillBlankQuiz extends Quiz<String> {

    @SerializedName(JsonAttributes.START)
    private final String mStart;

    @SerializedName(JsonAttributes.END)
    private final String mEnd;

    public FillBlankQuiz(String question, String answer, String start, String end) {
        super(question, answer);
        mStart = start;
        mEnd = end;
    }

    public String getStart() {
        return mStart;
    }

    public String getEnd() {
        return mEnd;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject quizJSON = super.toJSON();
        quizJSON.put(JsonAttributes.START, mStart);
        quizJSON.put(JsonAttributes.END, mEnd);
        return quizJSON;
    }
}
