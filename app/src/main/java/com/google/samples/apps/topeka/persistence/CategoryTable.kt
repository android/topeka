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

import android.provider.BaseColumns

/**
 * Structure of the category table.
 */
object CategoryTable {

    const val NAME = "category"

    const val COLUMN_ID = BaseColumns._ID
    const val COLUMN_NAME = "name"
    const val COLUMN_THEME = "theme"
    const val COLUMN_SCORES = "scores"
    const val COLUMN_SOLVED = "solved"

    @JvmField val PROJECTION = arrayOf(COLUMN_ID, COLUMN_NAME,
            COLUMN_THEME, COLUMN_SOLVED, COLUMN_SCORES)

    @JvmField val CREATE = """CREATE TABLE $NAME ($COLUMN_ID TEXT PRIMARY KEY,
                                $COLUMN_NAME TEXT NOT NULL, $COLUMN_THEME TEXT NOT NULL,
                                $COLUMN_SOLVED TEXT NOT NULL, $COLUMN_SCORES TEXT);"""
}
