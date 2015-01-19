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

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
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

    private Drawable mSuccessIcon;
    private Drawable mFailedIcon;

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
        final Resources resources = solvedState.getResources();
        final Drawable tintedImage;
        if (mCategory.isSolvedCorrectly(getItem(position))) {
            tintedImage = getSuccessIcon(resources);
        } else {
            tintedImage = getFailedIcon(resources);
        }
        solvedState.setImageDrawable(tintedImage);
    }

    private Drawable getSuccessIcon(Resources resources) {
        if (null == mSuccessIcon) {
            mSuccessIcon = loadAndTint(resources, R.drawable.ic_done, R.color.theme_green_primary);
        }
        return mSuccessIcon;
    }

    private Drawable getFailedIcon(Resources resources) {
        if (null == mFailedIcon) {
            mFailedIcon = loadAndTint(resources, R.drawable.ic_fail, R.color.theme_red_primary);
        }
        return mFailedIcon;
    }

    /**
     * Convenience method to aid tintint of vector drawables at runtime.
     *
     * @param resources The {@link Resources} for this app.
     * @param drawableId The id of the drawable to load.
     * @param tintColor The tint to apply.
     * @return The tinted drawable.
     */
    private Drawable loadAndTint(Resources resources, @DrawableRes int drawableId,
            @ColorRes int tintColor) {
        Drawable imageDrawable = resources.getDrawable(drawableId);
        imageDrawable.setTint(resources.getColor(tintColor));
        return imageDrawable;
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
