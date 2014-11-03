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
package com.google.samples.apps.topeka.widget;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import java.util.ArrayList;

public class ScorecardView extends ListView {

    public ScorecardView(Context context, Category category) {
        super(context);
        ArrayList<String> answers = new ArrayList<String>();
        for (Quiz quiz : category.getQuizzes()) {
            answers.add(quiz.getQuestion());
        }
        setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, answers));
    }


}
