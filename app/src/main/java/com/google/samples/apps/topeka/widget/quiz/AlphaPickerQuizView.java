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
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz;

import java.util.Arrays;
import java.util.List;

@SuppressLint("ViewConstructor")
public class AlphaPickerQuizView extends AbsQuizView<AlphaPickerQuiz> {

    private static final String KEY_SELECTION = "SELECTION";

    private TextView mCurrentSelection;
    private SeekBar mSeekBar;
    private List<String> mAlphabet;

    public AlphaPickerQuizView(Context context, Category category, AlphaPickerQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View createQuizContentView() {
        ScrollView layout = (ScrollView) getLayoutInflater().inflate(
                R.layout.quiz_layout_picker, this, false);
        mCurrentSelection = (TextView) layout.findViewById(R.id.seekbar_progress);
        mCurrentSelection.setText(getAlphabet().get(0));
        mSeekBar = (SeekBar) layout.findViewById(R.id.seekbar);
        mSeekBar.setMax(getAlphabet().size() - 1);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentSelection.setText(getAlphabet().get(progress));
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
        });
        return layout;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(mCurrentSelection.getText().toString());
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SELECTION, mCurrentSelection.getText().toString());
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        String userInput = savedInput.getString(KEY_SELECTION, getAlphabet().get(0));
        mSeekBar.setProgress(getAlphabet().indexOf(userInput));
    }


    private List<String> getAlphabet() {
        if (null == mAlphabet) {
            mAlphabet = Arrays.asList(getResources().getStringArray(R.array.alphabet));
        }
        return mAlphabet;
    }
}
