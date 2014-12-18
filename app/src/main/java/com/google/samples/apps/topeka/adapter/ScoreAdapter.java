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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import java.util.List;

/**
 * Adapter for displaying score cards.
 */
public class ScoreAdapter extends BaseAdapter {

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
            return AbsListView.INVALID_POSITION;
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
        setSolvedStateForQuiz(viewHolder.mSolvedState, position);
        return convertView;
    }

    private void setSolvedStateForQuiz(ImageView solvedState, int position) {
        final int imageResource;
        if (mCategory.isSolvedCorrectly(getItem(position))) {
            // TODO: 12/15/14 set the tint correctly
            imageResource = R.drawable.ic_done;
        } else {
            imageResource = R.drawable.ic_fail;
        }
        solvedState.setImageResource(imageResource);
    }

    private View initConvertView(ViewGroup parent) {
        View convertView;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LinearLayout scorecardItem = (LinearLayout) inflater.inflate(
                R.layout.item_scorecard, parent, false);
        convertView = scorecardItem;
        ViewHolder holder = new ViewHolder(scorecardItem);
        convertView.setTag(holder);
        return convertView;
    }

    private class ViewHolder {
        final TextView mAnswerView;
        final TextView mQuizView;
        final ImageView mSolvedState;

        public ViewHolder(LinearLayout scorecardItem) {
            scorecardItem.setBackgroundColor(scorecardItem.getResources().getColor(
                    mCategory.getTheme().getWindowBackgroundColor()));
            mQuizView = (TextView) scorecardItem.findViewById(R.id.quiz);
            mAnswerView = (TextView) scorecardItem.findViewById(R.id.answer);
            mSolvedState = (ImageView) scorecardItem.findViewById(R.id.solved_state);
        }

    }
}
