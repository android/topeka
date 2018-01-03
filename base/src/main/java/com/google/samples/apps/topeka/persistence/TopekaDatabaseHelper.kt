/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.topeka.persistence

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.samples.apps.topeka.base.R
import com.google.samples.apps.topeka.helper.toIntArray
import com.google.samples.apps.topeka.helper.toStringArray
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.JsonAttributes
import com.google.samples.apps.topeka.model.Theme
import com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz
import com.google.samples.apps.topeka.model.quiz.FillBlankQuiz
import com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz
import com.google.samples.apps.topeka.model.quiz.FourQuarterQuiz
import com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz
import com.google.samples.apps.topeka.model.quiz.OptionsQuiz
import com.google.samples.apps.topeka.model.quiz.PickerQuiz
import com.google.samples.apps.topeka.model.quiz.Quiz
import com.google.samples.apps.topeka.model.quiz.QuizType
import com.google.samples.apps.topeka.model.quiz.SelectItemQuiz
import com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz
import com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Arrays
import kotlin.collections.ArrayList

/**
 * Database for storing and retrieving info for categories and quizzes.
 */
class TopekaDatabaseHelper private constructor(
        context: Context
) : SQLiteOpenHelper(context.applicationContext, "topeka.db", null, 1) {

    private val resources: Resources = context.resources
    private var categories: MutableList<Category> = loadCategories()

    override fun onCreate(db: SQLiteDatabase) {
        with(db) {
            execSQL(CategoryTable.CREATE)
            execSQL(QuizTable.CREATE)
            fillCategoriesAndQuizzes(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    @Throws(JSONException::class, IOException::class)
    private fun fillCategoriesAndQuizzes(db: SQLiteDatabase) {
        db.transact {
            val values = ContentValues() // reduce, reuse
            val jsonArray = JSONArray(readCategoriesFromResources())
            for (i in 0 until jsonArray.length()) {
                with(jsonArray.getJSONObject(i)) {
                    val id = getString(JsonAttributes.ID)
                    fillCategory(db, values, this, id)
                    fillQuizzesForCategory(db, values, getJSONArray(JsonAttributes.QUIZZES), id)
                }
            }
        }
    }

    private fun readCategoriesFromResources(): String {
        val rawCategories = resources.openRawResource(R.raw.categories)
        val reader = BufferedReader(InputStreamReader(rawCategories))

        return reader.readText()
    }

    private fun fillCategory(db: SQLiteDatabase,
                             values: ContentValues,
                             category: JSONObject,
                             categoryId: String) {
        with(values) {
            clear()
            put(CategoryTable.COLUMN_ID, categoryId)
            put(CategoryTable.COLUMN_NAME, category.getString(JsonAttributes.NAME))
            put(CategoryTable.COLUMN_THEME, category.getString(JsonAttributes.THEME))
            put(CategoryTable.COLUMN_SOLVED, category.getString(JsonAttributes.SOLVED))
            put(CategoryTable.COLUMN_SCORES, category.getString(JsonAttributes.SCORES))
        }
        db.insert(CategoryTable.NAME, null, values)
    }

    private fun fillQuizzesForCategory(db: SQLiteDatabase,
                                       values: ContentValues,
                                       quizzes: JSONArray,
                                       categoryId: String) {
        var quiz: JSONObject
        for (i in 0 until quizzes.length()) {
            quiz = quizzes.getJSONObject(i)
            with(values) {
                clear()
                put(QuizTable.FK_CATEGORY, categoryId)
                put(QuizTable.COLUMN_TYPE, quiz.getString(JsonAttributes.TYPE))
                put(QuizTable.COLUMN_QUESTION, quiz.getString(JsonAttributes.QUESTION))
                put(QuizTable.COLUMN_ANSWER, quiz.getString(JsonAttributes.ANSWER))
            }
            putNonEmptyString(values, quiz, JsonAttributes.OPTIONS, QuizTable.COLUMN_OPTIONS)
            putNonEmptyString(values, quiz, JsonAttributes.MIN, QuizTable.COLUMN_MIN)
            putNonEmptyString(values, quiz, JsonAttributes.MAX, QuizTable.COLUMN_MAX)
            putNonEmptyString(values, quiz, JsonAttributes.START, QuizTable.COLUMN_START)
            putNonEmptyString(values, quiz, JsonAttributes.END, QuizTable.COLUMN_END)
            putNonEmptyString(values, quiz, JsonAttributes.STEP, QuizTable.COLUMN_STEP)
            db.insert(QuizTable.NAME, null, values)
        }
    }

    /**
     * Puts a non-empty string to ContentValues provided.

     * @param values     The place where the data should be put.
     *
     * @param quiz       The quiz potentially containing the data.
     *
     * @param jsonKey    The key to look for.
     *
     * @param contentKey The key use for placing the data in the database.
     */
    private fun putNonEmptyString(values: ContentValues,
                                  quiz: JSONObject,
                                  jsonKey: String,
                                  contentKey: String) {
        quiz.optString(jsonKey, null)?.let { values.put(contentKey, it) }
    }

    /**
     * Gets all categories with their quizzes.

     * @param fromDatabase `true` if a data refresh is needed, else `false`.
     *
     * @return All categories stored in the database.
     */
    fun getCategories(fromDatabase: Boolean = false): List<Category> {
        if (fromDatabase) {
            categories.clear()
            categories.addAll(loadCategories())
        }
        return categories
    }

    private fun loadCategories(): MutableList<Category> {
        val data = getCategoryCursor()
        val tmpCategories = ArrayList<Category>(data.count)
        do {
            tmpCategories.add(getCategory(data))
        } while (data.moveToNext())
        return tmpCategories
    }


    /**
     * Gets all categories wrapped in a [Cursor] positioned at it's first element.
     *
     * There are **no quizzes** within the categories obtained from this cursor.
     *
     * @return All categories stored in the database.
     */
    @SuppressLint("Recycle")
    private fun getCategoryCursor(): Cursor {
        synchronized(readableDatabase) {
            return readableDatabase
                    .query(CategoryTable.NAME, CategoryTable.PROJECTION,
                            null, null, null, null, null)
                    .also {
                        it.moveToFirst()
                    }
        }
    }

    /**
     * Gets a category from the given position of the cursor provided.

     * @param cursor           The Cursor containing the data.
     *
     * @return The found category.
     */
    private fun getCategory(cursor: Cursor): Category {
        // "magic numbers" based on CategoryTable#PROJECTION
        with(cursor) {
            val id = getString(0)
            val category = Category(
                    id = id,
                    name = getString(1),
                    theme = Theme.valueOf(getString(2)),
                    quizzes = getQuizzes(id, readableDatabase),
                    scores = JSONArray(getString(4)).toIntArray(),
                    solved = getBooleanFromDatabase(getString(3)))
            return category
        }
    }

    private fun getBooleanFromDatabase(isSolved: String?) =
            // Json stores booleans as true/false strings, whereas SQLite stores them as 0/1 values.
            isSolved?.length == 1 && Integer.valueOf(isSolved) == 1

    /**
     * Looks for a category with a given id.

     * @param categoryId Id of the category to look for.
     *
     * @return The found category.
     */
    fun getCategoryWith(categoryId: String): Category {
        val selectionArgs = arrayOf(categoryId)
        val data = readableDatabase
                .query(CategoryTable.NAME, CategoryTable.PROJECTION,
                        "${CategoryTable.COLUMN_ID}=?", selectionArgs,
                        null, null, null)
                .also { it.moveToFirst() }
        return getCategory(data)
    }

    /**
     * Scooooooooooore!

     * @return The score over all Categories.
     */
    fun getScore() = with(getCategories()) { sumBy { it.score } }

    /**
     * Updates values for a category.

     * @param category The category to update.
     */
    fun updateCategory(category: Category) {
        val index = categories.indexOfFirst { it.id == category.id }
        if (index != -1) {
            with (categories) {
                removeAt(index)
                add(index, category)
            }
        }

        val categoryValues = createContentValuesFor(category)
        writableDatabase.update(CategoryTable.NAME,
                categoryValues,
                "${CategoryTable.COLUMN_ID}=?",
                arrayOf(category.id))
        val quizzes = category.quizzes
        updateQuizzes(writableDatabase, quizzes)
    }

    /**
     * Updates a list of given quizzes.

     * @param writableDatabase The database to write the quizzes to.
     *
     * @param quizzes          The quizzes to write.
     */
    private fun updateQuizzes(writableDatabase: SQLiteDatabase, quizzes: List<Quiz<*>>) {
        var quiz: Quiz<*>
        val quizValues = ContentValues()
        val quizArgs = arrayOfNulls<String>(1)
        for (i in quizzes.indices) {
            quiz = quizzes[i]
            quizValues.clear()
            quizValues.put(QuizTable.COLUMN_SOLVED, quiz.solved)

            quizArgs[0] = quiz.question
            writableDatabase.update(QuizTable.NAME, quizValues,
                    "${QuizTable.COLUMN_QUESTION}=?", quizArgs)
        }
    }

    /**
     * Resets the contents of Topeka's database to it's initial state.
     */
    fun reset() {
        with(writableDatabase) {
            delete(CategoryTable.NAME, null, null)
            delete(QuizTable.NAME, null, null)
            fillCategoriesAndQuizzes(this)
        }
    }

    /**
     * Creates objects for quizzes according to a category id.

     * @param categoryId The category to create quizzes for.
     * @param database   The database containing the quizzes.
     * @return The found quizzes or an empty list if none were available.
     */
    @SuppressLint("Recycle")
    private fun getQuizzes(categoryId: String, database: SQLiteDatabase): List<Quiz<*>> {
        val quizzes = ArrayList<Quiz<*>>()

        val cursor = database.query(QuizTable.NAME,
                QuizTable.PROJECTION,
                "${QuizTable.FK_CATEGORY} LIKE ?", arrayOf(categoryId),
                null, null, null)
        cursor.moveToFirst()
        do quizzes.add(createQuizDueToType(cursor)) while (cursor.moveToNext())
        return quizzes
    }

    /**
     * Creates a quiz corresponding to the projection provided from a cursor row.
     * Currently only [QuizTable.PROJECTION] is supported.

     * @param cursor The Cursor containing the data.
     * @return The created quiz.
     */
    private fun createQuizDueToType(cursor: Cursor): Quiz<*> {
        // "magic numbers" based on QuizTable#PROJECTION
        val type = cursor.getString(2)
        val question = cursor.getString(3)
        val answer = cursor.getString(4)
        val options = cursor.getString(5)
        val min = cursor.getInt(6)
        val max = cursor.getInt(7)
        val step = cursor.getInt(8)
        val solved = getBooleanFromDatabase(cursor.getString(11))

        return when (type) {
            QuizType.ALPHA_PICKER.jsonName ->
                AlphaPickerQuiz(question, answer, solved)
            QuizType.FILL_BLANK.jsonName ->
                createFillBlankQuiz(cursor, question, answer, solved)
            QuizType.FILL_TWO_BLANKS.jsonName ->
                createFillTwoBlanksQuiz(question, answer, solved)
            QuizType.FOUR_QUARTER.jsonName ->
                createStringOptionsQuiz(question, answer, options, solved) {
                    q, a, o, s ->
                    FourQuarterQuiz(q, a, o, s)
                }
            QuizType.MULTI_SELECT.jsonName ->
                createStringOptionsQuiz(question, answer, options, solved) {
                    q, a, o, s ->
                    MultiSelectQuiz(q, a, o, s)
                }
            QuizType.PICKER.jsonName ->
                PickerQuiz(question, Integer.valueOf(answer)!!, min, max, step, solved)
            QuizType.SINGLE_SELECT.jsonName, QuizType.SINGLE_SELECT_ITEM.jsonName ->
                createStringOptionsQuiz(question, answer, options, solved) {
                    q, a, o, s ->
                    SelectItemQuiz(q, a, o, s)
                }
            QuizType.TOGGLE_TRANSLATE.jsonName ->
                createToggleTranslateQuiz(question, answer, options, solved)
            QuizType.TRUE_FALSE.jsonName -> TrueFalseQuiz(question, "true" == answer, solved)
            else -> throw IllegalArgumentException("Quiz type $type is not supported")
        }
    }

    private fun createFillBlankQuiz(cursor: Cursor,
                                    question: String,
                                    answer: String,
                                    solved: Boolean): Quiz<*> {
        val start = cursor.getString(9)
        val end = cursor.getString(10)
        return FillBlankQuiz(question, answer, start, end, solved)
    }

    private fun createFillTwoBlanksQuiz(question: String,
                                        answer: String,
                                        solved: Boolean): FillTwoBlanksQuiz {
        val answerArray = JSONArray(answer).toStringArray()
        return FillTwoBlanksQuiz(question, answerArray, solved)
    }

    private inline fun <T : OptionsQuiz<String>> createStringOptionsQuiz(
            question: String,
            answer: String,
            options: String,
            solved: Boolean,
            quizFactory: (String, IntArray, Array<String>, Boolean) -> T): T {
        val answerArray = JSONArray(answer).toIntArray()
        val optionsArray = JSONArray(options).toStringArray()
        return quizFactory(question, answerArray, optionsArray, solved)
    }

    private fun createToggleTranslateQuiz(question: String,
                                          answer: String,
                                          options: String,
                                          solved: Boolean): Quiz<*> {
        val answerArray = JSONArray(answer).toIntArray()
        val optionsArrays = extractOptionsArrays(options)
        return ToggleTranslateQuiz(question, answerArray, optionsArrays, solved)
    }

    private fun extractOptionsArrays(options: String): Array<Array<String>> {
        val optionsLvlOne = JSONArray(options).toStringArray()
        return Array(optionsLvlOne.size) { JSONArray(optionsLvlOne[it]).toStringArray() }
    }

    /**
     * Creates the content values to update a category in the database.

     * @param category The category to update.
     *
     * @return ContentValues containing updatable data.
     */
    private fun createContentValuesFor(category: Category) = ContentValues().apply {
        put(CategoryTable.COLUMN_SOLVED, category.solved)
        put(CategoryTable.COLUMN_SCORES, Arrays.toString(category.scores))
    }

    companion object {

        private var _instance: TopekaDatabaseHelper? = null

        fun getInstance(context: Context): TopekaDatabaseHelper {
            return _instance ?: synchronized(TopekaDatabaseHelper::class) {
                TopekaDatabaseHelper(context).also { _instance = it }
            }
        }
    }
}

inline fun SQLiteDatabase.transact(transaction: SQLiteDatabase.() -> Unit) {
    try {
        beginTransaction()
        transaction()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}
