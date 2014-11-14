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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.SelectItemQuiz;

import java.util.ArrayList;

public class SelectItemQuizView extends AbsQuizView<SelectItemQuiz>
        implements AdapterView.OnItemClickListener {

    private boolean[] mAnswers;

    public SelectItemQuizView(Context context, Category category, SelectItemQuiz quiz) {
        super(context, category, quiz);
        mAnswers = new boolean[quiz.getOptions().length];
    }

    @Override
    protected View getQuizContentView() {
        ListView layout = new ListView(getContext());
        layout.setAdapter(
                new ArrayAdapter<String>(getContext(), R.layout.item_answer,
                        getQuiz().getOptions()));
        layout.setOnItemClickListener(this);
        return layout;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(getCheckedAnswers());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        allowAnswer();
        toggleAnswerFor(position);
    }

    private void toggleAnswerFor(int answerId) {
        mAnswers[answerId] = !mAnswers[answerId];
    }

    private int[] getCheckedAnswers() {
        ArrayList<Integer> answers = new ArrayList<Integer>();
        for (int i = 0; i < mAnswers.length; i++) {
            if (mAnswers[i]) {
                answers.add(i);
            }
        }
        if (!answers.isEmpty()) {
            //manual int extraction to avoid boxing issues
            final int answersSize = answers.size();
            int[] answersArray = new int[answersSize];
            for (int i = 0; i < answersSize; i++) {
                answersArray[i] = answers.get(i);
            }
            return answersArray;
        }

        return null;
    }
}
