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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.ViewHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz;

public class FillBlankQuizView extends TextInputQuizView<FillBlankQuiz> {

    private EditText mAnswerView;

    public FillBlankQuizView(Context context, Category category, FillBlankQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        String start = getQuiz().getStart();
        String end = getQuiz().getEnd();
        if (null != start || null != end) {
            return getStartEndView(start, end);
        }
        if (null == mAnswerView) {
            mAnswerView = getEditText();
        }
        return mAnswerView;
    }

    private View getStartEndView(String start, String end) {
        LinearLayout container = inflateChildView(R.layout.quiz_fill_blank_with_surroundings);
        mAnswerView = ViewHelper.getView(container, R.id.quiz_edit_text);
        mAnswerView.addTextChangedListener(this);
        mAnswerView.setOnEditorActionListener(this);
        TextView startView = ViewHelper.getView(container, R.id.start);
        setExistingContentOrHide(startView, start);

        TextView endView = ViewHelper.getView(container, R.id.end);
        setExistingContentOrHide(endView, end);

        return container;
    }

    private void setExistingContentOrHide(TextView view, String content) {
        if (null == content) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(content);
        }
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(mAnswerView.getText().toString());
    }
}
