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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.Quiz;

public abstract class TextInputQuizView<Q extends Quiz> extends AbsQuizView<Q> implements
        TextWatcher {

    public TextInputQuizView(Context context, Category category, Q quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        return getEditText();
    }

    protected final EditText getEditText() {
        EditText editText = new EditText(getContext());
        editText.setTextAppearance(getContext(), android.R.style.TextAppearance_Material);
        setMinHeight(editText);
        editText.addTextChangedListener(this);
        return editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        setAnswered(after > 0);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /* no-op */
    }

    @Override
    public void afterTextChanged(Editable s) {
        /* no-op */
    }
}
