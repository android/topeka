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
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz;

public class TrueFalseQuizView extends AbsQuizView<TrueFalseQuiz> {

    public TrueFalseQuizView(Context context, Category category, TrueFalseQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        LinearLayout layout = new LinearLayout(getContext());
        addButtonWithText(layout, R.string.btn_true);
        addButtonWithText(layout, R.string.btn_false);
        return layout;
    }

    private void addButtonWithText(LinearLayout layout, @StringRes int textId) {
        layout.addView(getButton(textId), new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
    }

    private Button getButton(@StringRes int textId) {
        Button button = new Button(getContext());
        button.setText(textId);
        return button;
    }
}
