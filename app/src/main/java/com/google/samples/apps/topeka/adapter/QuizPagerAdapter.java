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
package com.google.samples.apps.topeka.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz;
import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz;
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz;
import com.google.samples.apps.topeka.model.quiz.FourQuarterQuiz;
import com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz;
import com.google.samples.apps.topeka.model.quiz.PickerQuiz;
import com.google.samples.apps.topeka.model.quiz.Quiz;
import com.google.samples.apps.topeka.model.quiz.SelectItemQuiz;
import com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz;
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz;
import com.google.samples.apps.topeka.widget.quiz.AbsQuizView;
import com.google.samples.apps.topeka.widget.quiz.AlphaPickerQuizView;
import com.google.samples.apps.topeka.widget.quiz.FillBlankQuizView;
import com.google.samples.apps.topeka.widget.quiz.FillTwoBlanksQuizView;
import com.google.samples.apps.topeka.widget.quiz.FourQuarterQuizView;
import com.google.samples.apps.topeka.widget.quiz.MultiSelectQuizView;
import com.google.samples.apps.topeka.widget.quiz.PickerQuizView;
import com.google.samples.apps.topeka.widget.quiz.SelectItemQuizView;
import com.google.samples.apps.topeka.widget.quiz.ToggleTranslateQuizView;
import com.google.samples.apps.topeka.widget.quiz.TrueFalseQuizView;

import java.util.List;

public class QuizPagerAdapter extends PagerAdapter {


    private final Context mContext;
    private final List<Quiz> mQuizzes;
    private final Category mCategory;
    private final ViewGroup.LayoutParams mLayoutParams;

    public QuizPagerAdapter(Context context, Category category) {
        mContext = context;
        mCategory = category;
        mQuizzes = category.getQuizzes();
        mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public int getCount() {
        return mQuizzes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Quiz quiz = mQuizzes.get(position);
        AbsQuizView view = getView(quiz);
        container.addView(view, mLayoutParams);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (null != container) {
            container.removeView((View) object);
        }
    }

    private AbsQuizView getView(Quiz quiz) {
        if (null == quiz) {
            throw new IllegalArgumentException("Quiz must not be null");
        }
        switch (quiz.getType()) {
            case ALPHA_PICKER:
                return new AlphaPickerQuizView(mContext, mCategory, (AlphaPickerQuiz) quiz);
            case FILL_BLANK:
                return new FillBlankQuizView(mContext, mCategory, (FillBlankQuiz) quiz);
            case FILL_TWO_BLANKS:
                return new FillTwoBlanksQuizView(mContext, mCategory, (FillTwoBlanksQuiz) quiz);
            case FOUR_QUARTER:
                return new FourQuarterQuizView(mContext, mCategory, (FourQuarterQuiz) quiz);
            case MULTI_SELECT:
                return new MultiSelectQuizView(mContext, mCategory, (MultiSelectQuiz) quiz);
            case PICKER:
                return new PickerQuizView(mContext, mCategory, (PickerQuiz) quiz);
            case SINGLE_SELECT:
            case SINGLE_SELECT_ITEM:
                return new SelectItemQuizView(mContext, mCategory, (SelectItemQuiz) quiz);
            case TOGGLE_TRANSLATE:
                return new ToggleTranslateQuizView(mContext, mCategory, (ToggleTranslateQuiz) quiz);
            case TRUE_FALSE:
                return new TrueFalseQuizView(mContext, mCategory, (TrueFalseQuiz) quiz);
        }
        throw new UnsupportedOperationException(
                "Quiz of type " + quiz.getType() + " can not be displayed.");
    }
}
