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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class OptionsQuiz<T> extends Quiz<int[]> {

    @SerializedName(JsonAttributes.OPTIONS)
    private T[] mOptions;

    public OptionsQuiz(String question, int[] answer, T[] options) {
        super(question, answer);
        mOptions = options;
    }

    public T[] getOptions() {
        return mOptions;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject quizJSON = super.toJSON();
        JSONArray optionsJSON = new JSONArray();
        for (T option : mOptions) {
            optionsJSON.put(option);
        }
        quizJSON.put(com.google.samples.apps.topeka.model.JsonAttributes.OPTIONS, optionsJSON);
        return quizJSON;
    }
}
