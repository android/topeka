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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.JsonAttributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Database for storing and retrieving info for categories and quizzes
 */
public class TopekaDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "TopekaDatabaseHelper";
    private static final String DB_NAME = "topeka";
    private static final String DB_SUFFIX = ".db";
    private static final int DB_VERSION = 1;

    private static final String SQL_DATABASE_CREATE = CategoryTable.CREATE;
    private static Category[] mCategories;
    private static TopekaDatabaseHelper mInstance;
    private final Resources mResources;

    private TopekaDatabaseHelper(Context context) {
        //prevents external instance creation
        super(context, DB_NAME + DB_SUFFIX, null, DB_VERSION);
        mResources = context.getResources();
    }

    private static TopekaDatabaseHelper getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new TopekaDatabaseHelper(context);
        }
        return mInstance;
    }

    /**
     * Gets all categories wrapped in a {@link CategoryCursor}.
     *
     * @param context The context this is running in.
     * @return All categories stored in the database.
     */
    public static CategoryCursor getCategoryCursor(Context context) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        Cursor data = readableDatabase
                .query(CategoryTable.NAME, CategoryTable.PROJECTION, null, null, null, null, null);
        return new CategoryCursor(data);
    }

    /**
     * Gets all categories.
     *
     * @param context The context this is running in.
     * @return All categories stored in the database.
     */
    public static Category[] getAllCategories(Context context) {
        if (null == mCategories) {
            CategoryCursor categoryCursor = TopekaDatabaseHelper.getCategoryCursor(context);
            if (categoryCursor.isBeforeFirst()) {
                categoryCursor.moveToFirst();
            }
            mCategories = new Category[categoryCursor.getCount()];
            do {
                mCategories[categoryCursor.getPosition()] = categoryCursor.getCategory();
            } while (categoryCursor.moveToNext());
        }
        return mCategories;
    }

    /**
     * Scooooooooooore!
     *
     * @param context The context this is running in.
     * @return The score over all Categories.
     */
    public static int getScore(Context context) {
        Category[] allCategories = getAllCategories(context);
        int score = 0;
        for (Category cat : allCategories) {
            score += cat.getScore();
        }
        return score;
    }

    public static void updateCategory(Context context, Category category) {
        SQLiteDatabase writableDatabase = getWriteableDatabase(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoryTable.COLUMN_DATA, new Gson().toJson(category));
        writableDatabase.update(CategoryTable.NAME, contentValues, CategoryTable.COLUMN_ID + "= ?",
                new String[]{category.getId()});
    }

    /**
     * Looks for a category with a given id.
     *
     * @param context The context this is running in.
     * @param categoryId Id of the category to look for.
     * @return The found category.
     */
    public static Category getCategoryWith(Context context, String categoryId) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        String[] selectionArgs = {categoryId};
        Cursor data = readableDatabase
                .query(CategoryTable.NAME, CategoryTable.PROJECTION, CategoryTable.COLUMN_ID + "=?",
                        selectionArgs, null, null, null);
        CategoryCursor categoryCursor = new CategoryCursor(data);
        categoryCursor.moveToFirst();
        return categoryCursor.getCategory();
    }

    /**
     * Resets the contents of Topeka's database to it's initial state.
     * @param context The context this is running in.
     */
    public static void reset(Context context) {
        SQLiteDatabase writeableDatabase = getWriteableDatabase(context);
        writeableDatabase.delete(CategoryTable.NAME, null, null);
        getInstance(context).preFillDatabase(writeableDatabase);
    }

    private static SQLiteDatabase getReadableDatabase(Context context) {
        return getInstance(context).getReadableDatabase();
    }

    private static SQLiteDatabase getWriteableDatabase(Context context) {
        return getInstance(context).getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DATABASE_CREATE);
        preFillDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* no-op */
    }

    private void preFillDatabase(SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            JSONArray jsonArray = new JSONArray(readCategoriesFromResources());
            JSONObject category;
            for (int i = 0; i < jsonArray.length(); i++) {
                category = jsonArray.getJSONObject(i);
                values.put(CategoryTable.COLUMN_ID, category.getString(JsonAttributes.ID));
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

}
