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
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz;

public class ToggleTranslateQuizView extends AbsQuizView<ToggleTranslateQuiz> {

    private LayoutParams mOptionsParams;

    public ToggleTranslateQuizView(Context context, Category category, ToggleTranslateQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        if (null == mOptionsParams) {
            mOptionsParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        for (String[] option : getQuiz().getOptions()) {
            layout.addView(getOptionsView(option), mOptionsParams);
        }
        return layout;
    }

    private Button getOptionsView(String[] options) {
        if (null == options || options.length != 2) {
            throw new IllegalArgumentException("The options provided were invalid: " + options);
        }
        Button button = new Button(getContext());
        button.setText(options[0] + "<>" + options[1]);
        return button;
    }
}
