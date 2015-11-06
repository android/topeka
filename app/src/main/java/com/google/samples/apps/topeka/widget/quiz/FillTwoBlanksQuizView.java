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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz;

@SuppressLint("ViewConstructor")
public class FillTwoBlanksQuizView extends TextInputQuizView<FillTwoBlanksQuiz> {

    private static final String KEY_ANSWER_ONE = "ANSWER_ONE";
    private static final String KEY_ANSWER_TWO = "ANSWER_TWO";
    private static final LinearLayout.LayoutParams CHILD_LAYOUT_PARAMS
            = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
    private EditText mAnswerOne;
    private EditText mAnswerTwo;

    public FillTwoBlanksQuizView(Context context, Category category, FillTwoBlanksQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View createQuizContentView() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        mAnswerOne = createEditText();
        mAnswerOne.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mAnswerTwo = createEditText();
        mAnswerTwo.setId(R.id.quiz_edit_text_two);
        addEditText(layout, mAnswerOne);
        addEditText(layout, mAnswerTwo);
        return layout;
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ANSWER_ONE, mAnswerOne.getText().toString());
        bundle.putString(KEY_ANSWER_TWO, mAnswerTwo.getText().toString());
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        mAnswerOne.setText(savedInput.getString(KEY_ANSWER_ONE));
        mAnswerTwo.setText(savedInput.getString(KEY_ANSWER_TWO));
    }

    private void addEditText(LinearLayout layout, EditText editText) {
        layout.addView(editText, CHILD_LAYOUT_PARAMS);
    }

    @Override
    protected boolean isAnswerCorrect() {
        String partOne = getAnswerFrom(mAnswerOne);
        String partTwo = getAnswerFrom(mAnswerTwo);
        return getQuiz().isAnswerCorrect(new String[]{partOne, partTwo});
    }

    private String getAnswerFrom(EditText view) {
        return view.getText().toString();
    }
}
