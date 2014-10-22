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

package com.google.samples.apps.topeka.model;

import com.google.gson.annotations.SerializedName;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable {

    public static final String TAG = "Category";

    @SerializedName(JsonAttributes.NAME)
    private final String mName;

    @SerializedName(JsonAttributes.ID)
    private final String mId;

    @SerializedName(JsonAttributes.THEME)
    private final Theme mTheme;

    @SerializedName(JsonAttributes.QUIZZES)
    private final List<Quiz> mQuizzes;

    public Category(String name, String id, Theme theme, List<Quiz> quizzes) {
        mName = name;
        mId = id;
        mTheme = theme;
        mQuizzes = quizzes;
    }

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }

    public Theme getTheme() {
        return mTheme;
    }

    public List<Quiz> getQuizzes() {
        return mQuizzes;
    }

    protected Category(Parcel in) {
        mName = in.readString();
        mId = in.readString();
        mTheme = Theme.values()[in.readInt()];
        mQuizzes = new ArrayList<Quiz>();
        in.readTypedList(mQuizzes, Quiz.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mId);
        dest.writeInt(mTheme.ordinal());
        dest.writeTypedList(getQuizzes());
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public String toString() {
        return "Category{" +
                "mName='" + mName + '\'' +
                ", mId='" + mId + '\'' +
                ", mTheme=" + mTheme +
                ", mQuizzes=" + mQuizzes +
                '}';
    }
}
