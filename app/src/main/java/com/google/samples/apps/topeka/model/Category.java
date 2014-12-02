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
import com.google.samples.apps.topeka.helper.ParcelableHelper;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Category implements Parcelable {

    public static final String TAG = "Category";

    private static final int SCORE = 8;
    private static final int NO_SCORE = 0;

    @SerializedName(JsonAttributes.NAME)
    private final String mName;
    @SerializedName(JsonAttributes.ID)
    private final String mId;
    @SerializedName(JsonAttributes.THEME)
    private final Theme mTheme;
    @SerializedName(JsonAttributes.QUIZZES)
    private final List<Quiz> mQuizzes;
    @SerializedName(JsonAttributes.SCORES)
    private final int[] mScores;
    @SerializedName(JsonAttributes.SOLVED)
    private boolean mSolved;

    public Category(String name, String id, Theme theme, List<Quiz> quizzes) {
        mName = name;
        mId = id;
        mTheme = theme;
        mQuizzes = quizzes;
        mScores = new int[quizzes.size()];
        mSolved = false;
    }


    protected Category(Parcel in) {
        mName = in.readString();
        mId = in.readString();
        mTheme = Theme.values()[in.readInt()];
        mQuizzes = new ArrayList<Quiz>();
        in.readTypedList(mQuizzes, Quiz.CREATOR);
        mScores = in.createIntArray();
        mSolved = ParcelableHelper.readBoolean(in);
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

    /**
     * Updates a score for a provided quiz within this category.
     *
     * @param which The quiz to rate.
     * @param correctlySolved <code>true</code> if the quiz was solved else <code>false</code>.
     */
    public void setScore(Quiz which, boolean correctlySolved) {
        int index = mQuizzes.indexOf(which);
        if (-1 == index) {
            return;
        }
        mScores[index] = correctlySolved ? SCORE : NO_SCORE;
    }

    /**
     * Gets the score for a single quiz.
     *
     * @param which The quiz to look for
     * @return The score if found, else 0.
     */
    public int getScore(Quiz which) {
        try {
            return mScores[mQuizzes.indexOf(which)];
        } catch (IndexOutOfBoundsException ioobe) {
            return 0;
        }
    }

    /**
     * @return The sum of all quiz scores within this category.
     */
    public int getScore() {
        int categoryScore = 0;
        for (int quizScore : mScores) {
            categoryScore += quizScore;
        }
        return categoryScore;
    }

    public boolean isSolved() {
        return mSolved;
    }


    public void setSolved(boolean solved) {
        this.mSolved = solved;
    }

    @Override
    public String toString() {
        return "Category{" +
                "mName='" + mName + '\'' +
                ", mId='" + mId + '\'' +
                ", mTheme=" + mTheme +
                ", mQuizzes=" + mQuizzes +
                ", mScores=" + Arrays.toString(mScores) +
                ", mSolved=" + mSolved +
                '}';
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mId);
        dest.writeInt(mTheme.ordinal());
        dest.writeTypedList(getQuizzes());
        dest.writeIntArray(mScores);
        ParcelableHelper.writeBoolean(dest, mSolved);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category category = (Category) o;

        if (!mId.equals(category.mId)) {
            return false;
        }
        if (!mName.equals(category.mName)) {
            return false;
        }
        if (!mQuizzes.equals(category.mQuizzes)) {
            return false;
        }
        if (mTheme != category.mTheme) {
            return false;
        }
        if (mSolved != category.mSolved) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mId.hashCode();
        result = 31 * result + mTheme.hashCode();
        result = 31 * result + mQuizzes.hashCode();
        result = 31 * result + (mSolved ? 1 : 0);
        return result;
    }
}
