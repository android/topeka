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

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.Quiz;

public abstract class TextInputQuizView<Q extends Quiz> extends AbsQuizView<Q>
        implements TextWatcher, TextView.OnEditorActionListener {

    public TextInputQuizView(Context context, Category category, Q quiz) {
        super(context, category, quiz);
    }

    protected final EditText createEditText() {
        EditText editText = (EditText) getLayoutInflater().inflate(
                R.layout.quiz_edit_text, this, false);
        editText.addTextChangedListener(this);
        editText.setOnEditorActionListener(this);
        return editText;
    }

    @Override
    protected void submitAnswer() {
        hideKeyboard(this);
        super.submitAnswer();
    }

    /**
     * Convenience method to hide the keyboard.
     *
     * @param view A view in the hierarchy.
     */
    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = getInputMethodManager();
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (TextUtils.isEmpty(v.getText())) {
            return false;
        }
        allowAnswer();
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            submitAnswer();
            hideKeyboard(v);
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        /* no-op */
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /* no-op */
    }

    @Override
    public void afterTextChanged(Editable s) {
        allowAnswer(!TextUtils.isEmpty(s));
    }
}
