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
import android.widget.Toolbar;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.widget.DoneFab;
import com.google.samples.apps.topeka.widget.FloatingActionButton;


public abstract class AbsQuizView<Q extends Quiz> extends LinearLayout implements
        View.OnClickListener {

    private final FloatingActionButton mSubmitAnswer;
    private final Toolbar mToolbar;
    private Q mQuiz;

    public AbsQuizView(Context context, Category category, Q quiz) {
        super(context);
        mQuiz = quiz;
        setOrientation(VERTICAL);

        mToolbar = new Toolbar(context);
        mToolbar.setTitle(getQuiz().getQuestion());
        int backgroundColor = getResources()
                .getColor(category.getTheme().getWindowBackgroundColor());
        mToolbar.setBackgroundColor(backgroundColor);
        addView(mToolbar,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mSubmitAnswer = new DoneFab(context);
        mSubmitAnswer.setId(R.id.submitAnswer);
        addView(getQuizContentView(),
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    protected abstract View getQuizContentView();

    public Q getQuiz() {
        return mQuiz;
    }

    protected void setAnswered(boolean answered) {
        if (answered) {
            mSubmitAnswer.setVisibility(View.VISIBLE);
        } else {
            mSubmitAnswer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitAnswer: {
                //TODO: 11/3/14 handle
                break;
            }
        }
    }
}
