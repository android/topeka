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
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz;

public class MultiSelectQuizView extends AbsQuizView<MultiSelectQuiz>
        implements CompoundButton.OnCheckedChangeListener {

    private LayoutParams mOptionsParams;

    public MultiSelectQuizView(Context context, Category category, MultiSelectQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View getQuizContentView() {
        if (null == mOptionsParams) {
            mOptionsParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        ScrollView scrollView = new ScrollView(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        String[] options = getQuiz().getOptions();
        for (String option : options) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setTextAppearance(getContext(),
                    android.R.style.TextAppearance_Material_Subhead);
            checkBox.setText(option);
            setMinHeight(checkBox);
            layout.addView(checkBox, mOptionsParams);
        }

        scrollView.addView(layout, mOptionsParams);
        return scrollView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        answerQuiz();
    }
}
