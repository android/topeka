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

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.QuizAdapter;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Database for storing and retrieving info for categories and quizzes
 */
public class TopekaDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "TopekaDatabaseHelper";
    private static final String SQL_DATABASE_CREATE = "create table " + CategoryTable.NAME + "("
            + CategoryTable.COLUMN_ID + " integer primary key autoincrement, "
            + CategoryTable.COLUMN_DATA + " text not null);";
    private static final String DB_NAME = "topeka";
    private static final String DB_SUFFIX = ".db";
    private static final int DB_VERSION = 1;
    private final Resources mResources;

    public TopekaDatabaseHelper(Context context) {
        super(context, DB_NAME + DB_SUFFIX, null, DB_VERSION);
        mResources = context.getResources();
    }

    public static CategoryCursor getCategoryCursor(Context context) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        Cursor data = readableDatabase.query(CategoryTable.NAME,
                CategoryTable.PROJECTION, null, null, null,
                null, null);
        return new CategoryCursor(data);
    }

    public static Category[] getAllCategories(Context context) {
        TopekaDatabaseHelper.CategoryCursor categoryCursor = TopekaDatabaseHelper
                .getCategoryCursor(context);
        if (categoryCursor.isBeforeFirst()) {
            categoryCursor.moveToFirst();
        }
        Category categories[] = new Category[categoryCursor.getCount()];
        do {
            categories[categoryCursor.getPosition()] = categoryCursor.getCategory();
        } while (categoryCursor.moveToNext());
        return categories;
    }

    public static Category getCategoryAt(Context context, int position) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        Cursor data = readableDatabase
                .query(CategoryTable.NAME, CategoryTable.PROJECTION, CategoryTable.COLUMN_ID + "=?",
                        new String[]{String.valueOf(position)}, null, null, null);
        CategoryCursor categoryCursor = new CategoryCursor(data);
        categoryCursor.moveToFirst();
        return categoryCursor.getCategory();
    }

    private static SQLiteDatabase getReadableDatabase(Context context) {
        return new TopekaDatabaseHelper(context).getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DATABASE_CREATE);
        preFillDatabase(db);
    }

    private void preFillDatabase(SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            JSONArray jsonArray = new JSONArray(readCategoriesFromResources());
            JSONObject category;
            for (int i = 0; i < jsonArray.length(); i++) {
                category = jsonArray.getJSONObject(i);
                values.put(CategoryTable.COLUMN_DATA, category.toString());
                db.insert(CategoryTable.NAME, null, values);
            }
        } catch (IOException e) {
            //TODO: 10/23/14 we're probably not recoverable any more here. Recommend re-installation?
            Log.e(TAG, "onCreate", e);
        } catch (JSONException e) {
            //TODO: 10/23/14 we're probably not recoverable any more here. Recommend re-installation?
            Log.e(TAG, "onCreate", e);
        }
    }

    private String readCategoriesFromResources() throws IOException {
        StringBuilder categoriesJson = new StringBuilder();
        InputStream rawCategories = mResources.openRawResource(R.raw.categories);
        BufferedReader reader = new BufferedReader(new InputStreamReader(rawCategories));
        String line;

        while ((line = reader.readLine()) != null) {
            categoriesJson.append(line);
        }
        return categoriesJson.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* no-op */
    }

    public interface CategoryTable {

        static final String NAME = "category";

        static final String COLUMN_ID = BaseColumns._ID;
        static final String COLUMN_DATA = "data";
        public static final String[] PROJECTION = new String[]{CategoryTable.COLUMN_ID,
                CategoryTable.COLUMN_DATA};
    }

    /**
     * Wraps a {@link android.database.Cursor} to provide access to categories, ids and quizzes more easily.
     */
    public static class CategoryCursor extends CursorWrapper {

        private Gson mGson;

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public CategoryCursor(Cursor cursor) {
            super(cursor);
        }

        public int getId() {
            return getInt(getColumnIndex(CategoryTable.COLUMN_ID));
        }

        public Category getCategory() {
            return getCustomizedGson()
                    .fromJson(getString(getColumnIndex(CategoryTable.COLUMN_DATA)), Category.class);
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
}
