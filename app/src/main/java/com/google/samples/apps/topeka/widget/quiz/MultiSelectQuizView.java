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
package com.google.samples.apps.topeka.widget.quiz;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz;

public class MultiSelectQuizView extends AbsQuizView<MultiSelectQuiz> {

    private LayoutParams mOptionsParams;

    public MultiSelectQuizView(Context context, Category category, MultiSelectQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        if (null == mOptionsParams) {
            mOptionsParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(VERTICAL);
        for (String option : getQuiz().getOptions()) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(option);
            layout.addView(checkBox, mOptionsParams);
        }

        return layout;
    }
}
