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
import android.widget.ListView;

import com.google.samples.apps.topeka.adapter.OptionsQuizAdapter;
import com.google.samples.apps.topeka.helper.AnswerHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz;

@SuppressLint("ViewConstructor")
public class MultiSelectQuizView extends AbsQuizView<MultiSelectQuiz> {

    private static final String KEY_ANSWER = "ANSWER";

    private ListView mListView;

    public MultiSelectQuizView(Context context, Category category, MultiSelectQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View createQuizContentView() {
        mListView = new ListView(getContext());
        mListView.setAdapter(
                new OptionsQuizAdapter(getQuiz().getOptions(),
                        android.R.layout.simple_list_item_multiple_choice));
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setItemsCanFocus(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allowAnswer();
            }
        });
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
        boolean[] bundleableAnswer = getBundleableAnswer();
        bundle.putBooleanArray(KEY_ANSWER, bundleableAnswer);
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        final boolean[] answers = savedInput.getBooleanArray(KEY_ANSWER);
        if (null == answers) {
            return;
        }
        for (int i = 0; i < answers.length; i++) {
            mListView.setItemChecked(i, answers[i]);
        }
    }

    private boolean[] getBundleableAnswer() {
        SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
        final int answerSize = checkedItemPositions.size();
        if (0 == answerSize) {
            return null;
        }
        final int optionsSize = getQuiz().getOptions().length;
        boolean[] bundleableAnswer = new boolean[optionsSize];
        int key;
        for (int i = 0; i < answerSize; i++) {
            key = checkedItemPositions.keyAt(i);
            bundleableAnswer[key] = checkedItemPositions.valueAt(i);
        }
        return bundleableAnswer;
    }
}
