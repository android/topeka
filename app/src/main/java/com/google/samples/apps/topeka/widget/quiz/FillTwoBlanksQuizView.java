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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz;

public class FillTwoBlanksQuizView extends TextInputQuizView<FillTwoBlanksQuiz> {

    private final EditText mAnswerOne;
    private final EditText mAnswerTwo;

    public FillTwoBlanksQuizView(Context context, Category category, FillTwoBlanksQuiz quiz) {
        super(context, category, quiz);
        mAnswerOne = getEditText();
        mAnswerOne.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mAnswerTwo = getEditText();
    }

    @Override
    protected View getQuizContentView() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        addEditText(layout, mAnswerOne);
        addEditText(layout, mAnswerTwo);
        return layout;
    }

    private void addEditText(LinearLayout layout, EditText editText) {
        layout.addView(editText, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
    }
}
