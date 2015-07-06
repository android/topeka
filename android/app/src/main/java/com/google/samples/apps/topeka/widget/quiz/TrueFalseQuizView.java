/*
 * Copyright 2015 Google Inc.
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz;

@SuppressLint("ViewConstructor")
public class TrueFalseQuizView extends AbsQuizView<TrueFalseQuiz> {

    private static final String KEY_SELECTION = "SELECTION";
    private static final LinearLayout.LayoutParams LAYOUT_PARAMS =
            new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);

    static {
        LAYOUT_PARAMS.gravity = Gravity.CENTER;
    }

    private boolean mAnswer;
    private View mAnswerTrue;
    private View mAnswerFalse;

    public TrueFalseQuizView(Context context, Category category, TrueFalseQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View createQuizContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.quiz_radio_group_true_false, this, false);

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.answerTrue:
                        mAnswer = true;
                        break;
                    case R.id.answerFalse:
                        mAnswer = false;
                        break;
                }
                allowAnswer();
            }
        };

        mAnswerTrue = container.findViewById(R.id.answerTrue);
        mAnswerTrue.setOnClickListener(clickListener);
        mAnswerFalse = container.findViewById(R.id.answerFalse);
        mAnswerFalse.setOnClickListener(clickListener);
        return container;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(mAnswer);
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SELECTION, mAnswer);
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        final boolean tmpAnswer = savedInput.getBoolean(KEY_SELECTION);
        performSelection(tmpAnswer ? mAnswerTrue : mAnswerFalse);
    }

    private void performSelection(View selection) {
        selection.performClick();
        selection.setSelected(true);
    }
}
