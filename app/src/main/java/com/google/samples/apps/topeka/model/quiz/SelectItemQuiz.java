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

import com.google.samples.apps.topeka.model.quiz.abstracts.OptionsQuiz;

public class SelectItemQuiz extends OptionsQuiz<String> {

    public SelectItemQuiz(String question, int[] answer, String[] options) {
        super(question, answer, options);
    }

    protected SelectItemQuiz(Parcel in) {
        super(in);
        int[] answer = in.createIntArray();
        in.readIntArray(answer);
        setAnswer(answer);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeIntArray(getAnswer());
    }
}
