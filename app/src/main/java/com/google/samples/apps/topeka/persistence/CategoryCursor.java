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
package com.google.samples.apps.topeka.persistence;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.QuizAdapter;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import java.util.List;

/**
 * Wraps a {@link android.database.Cursor} to provide access to categories, ids and quizzes more
 * easily.
 */
public class CategoryCursor extends CursorWrapper {

    private Gson mGson;

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CategoryCursor(Cursor cursor) {
        super(cursor);
    }

    public String getId() {
        return getString(getColumnIndex(CategoryTable.COLUMN_ID));
    }

    public Category getCategory() {
        int columnIndex = getColumnIndex(CategoryTable.COLUMN_DATA);
        return getCustomizedGson().fromJson(getString(columnIndex), Category.class);
    }

    public List<Quiz> getQuizzes() {
        return getCategory().getQuizzes();
    }

    private Gson getCustomizedGson() {
        if (null == mGson) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Quiz.class, new QuizAdapter());
            mGson = gsonBuilder.create();
        }
        return mGson;
    }
}
