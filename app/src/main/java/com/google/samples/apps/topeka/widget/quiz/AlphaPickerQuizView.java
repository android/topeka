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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.ViewHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz;

public class AlphaPickerQuizView extends AbsQuizView<AlphaPickerQuiz> implements
        SeekBar.OnSeekBarChangeListener {

    private TextView mCurrentSelection;

    private final String[] mAlphabet;

    public AlphaPickerQuizView(Context context, Category category, AlphaPickerQuiz quiz) {
        super(context, category, quiz);
        mAlphabet = getResources().getStringArray(R.array.alphabet);
    }

    @Override
    protected View getQuizContentView() {
        LinearLayout layout = inflateChildView(R.layout.quiz_layout_picker);
        mCurrentSelection = ViewHelper.getView(layout, R.id.seekbar_progress);
        mCurrentSelection.setText(mAlphabet[0]);
        SeekBar seekBar = ViewHelper.getView(layout, R.id.seekbar);
        seekBar.setMax(mAlphabet.length);
        seekBar.setOnSeekBarChangeListener(this);
        return layout;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect((String) mCurrentSelection.getText());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCurrentSelection.setText(mAlphabet[progress]);
        allowAnswer();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        /* no-op */
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        /* no-op */
    }
}
