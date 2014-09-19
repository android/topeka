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

public final class MinMaxQuiz extends Quiz<Integer> {

    @SerializedName(JsonAttributes.MIN)
    private final int mMin;

    @SerializedName(JsonAttributes.MAX)
    private final int mMax;

    public MinMaxQuiz(String question, Integer answer, int min, int max) {
        super(question, answer);
        mMin = min;
        mMax = max;
    }

    public int getMin() {
        return mMin;
    }

    public int getMax() {
        return mMax;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject quizJSON = super.toJSON();
        quizJSON.put(com.google.samples.apps.topeka.model.JsonAttributes.MIN, mMin);
        quizJSON.put(com.google.samples.apps.topeka.model.JsonAttributes.MAX, mMax);
        return quizJSON;
    }
}
