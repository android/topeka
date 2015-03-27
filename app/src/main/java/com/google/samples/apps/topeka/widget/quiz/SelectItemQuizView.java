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
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.AnswerHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.SelectItemQuiz;
import com.google.samples.apps.topeka.adapter.OptionsQuizAdapter;

@SuppressLint("ViewConstructor")
public class SelectItemQuizView extends AbsQuizView<SelectItemQuiz>
        implements AdapterView.OnItemClickListener {

    private static final String KEY_ANSWERS = "ANSWERS";

    private boolean[] mAnswers;
    private ListView mListView;

    public SelectItemQuizView(Context context, Category category, SelectItemQuiz quiz) {
        super(context, category, quiz);
        mAnswers = new boolean[quiz.getOptions().length];
    }

    @Override
    protected View createQuizContentView() {
        mListView = new ListView(getContext());
        mListView.setDivider(null);
        mListView.setSelector(R.drawable.selector_button);
        mListView.setAdapter(
                new OptionsQuizAdapter(getQuiz().getOptions(), R.layout.item_answer_start,
                        getContext(), true));
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(this);
        return mListView;
    }

    @Override
    protected boolean isAnswerCorrect() {
        final SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
        final int[] answer = getQuiz().getAnswer();
        return AnswerHelper.isAnswerCorrect(checkedItemPositions, answer);
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putBooleanArray(KEY_ANSWERS, mAnswers);
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        // FIXME: 3/10/15 make sure the ui reflects the selected state
        mAnswers = savedInput.getBooleanArray(KEY_ANSWERS);
        final ListAdapter adapter = mListView.getAdapter();
        for (int i = 0; i < mAnswers.length; i++) {
            mListView.performItemClick(mListView.getChildAt(i), i, adapter.getItemId(i));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        allowAnswer();
        toggleAnswerFor(position);
    }

    private void toggleAnswerFor(int answerId) {
        mAnswers[answerId] = !mAnswers[answerId];
    }
}
