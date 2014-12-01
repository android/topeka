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
package com.google.samples.apps.topeka.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.ViewHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import java.util.List;

public class ScorecardView extends ListView {

    public ScorecardView(Context context) {
        super(context);
    }

    public ScorecardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScorecardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCategory(Category category) {
        setAdapter(new ScoreAdapter(category));
    }

    private class ScoreAdapter extends BaseAdapter {

        private final Category mCategory;
        private final int count;
        private final List<Quiz> mQuizList;

        public ScoreAdapter(Category category) {
            mCategory = category;
            mQuizList = mCategory.getQuizzes();
            count = mQuizList.size();
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Quiz getItem(int position) {
            return mQuizList.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (position > count || position < 0) {
                return INVALID_POSITION;
            }
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = initConvertView(parent);
            }

            final Quiz quiz = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mQuizView.setText(quiz.getQuestion());
            viewHolder.mAnswerView.setText(quiz.getStringAnswer());
            return convertView;
        }

        private View initConvertView(ViewGroup parent) {
            View convertView;
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            LinearLayout scorecardItem = (LinearLayout) inflater
                    .inflate(R.layout.item_scorecard, parent, false);
            convertView = scorecardItem;
            ViewHolder holder = new ViewHolder(scorecardItem);
            convertView.setTag(holder);
            return convertView;
        }

        private class ViewHolder {

            final LinearLayout mScorecardItem;
            final TextView mAnswerView;
            final TextView mQuizView;

            public ViewHolder(LinearLayout scorecardItem) {
                mScorecardItem = scorecardItem;
                mQuizView = ViewHelper.getView(scorecardItem, R.id.quiz);
                mAnswerView = ViewHelper.getView(scorecardItem, R.id.answer);
            }

        }
    }
}
