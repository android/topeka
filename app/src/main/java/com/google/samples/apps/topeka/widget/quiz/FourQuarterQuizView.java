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
import android.widget.GridView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.FourQuarterQuiz;
import com.google.samples.apps.topeka.adapter.OptionsQuizAdapter;

public class FourQuarterQuizView extends AbsQuizView<FourQuarterQuiz>
        implements AdapterView.OnItemClickListener {

    private int mAnswered = -1;

    public FourQuarterQuizView(Context context, Category category, FourQuarterQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        GridView layout = new GridView(getContext());
        layout.setNumColumns(2);
        layout.setAdapter(new OptionsQuizAdapter(getQuiz().getOptions(), R.layout.item_answer));
        layout.setOnItemClickListener(this);
        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        allowAnswer();
        mAnswered = position;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(new int[]{mAnswered});
    }
}
