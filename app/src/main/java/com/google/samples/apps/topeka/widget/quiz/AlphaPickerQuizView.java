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

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz;

public class AlphaPickerQuizView extends AbsQuizView<AlphaPickerQuiz> implements
        SeekBar.OnSeekBarChangeListener {

    private static final CharSequence[] ALPHABET = new CharSequence[]
            {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                    "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private TextView mCurrentSelection;
    private SeekBar mSeekBar;

    public AlphaPickerQuizView(Context context, Category category, AlphaPickerQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        mCurrentSelection = new TextView(getContext());
        layout.addView(mCurrentSelection, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 2));
        mCurrentSelection.setText(ALPHABET[0]);
        mSeekBar = new SeekBar(getContext());
        mSeekBar.setMax(ALPHABET.length);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        return layout;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCurrentSelection.setText(ALPHABET[progress]);
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
