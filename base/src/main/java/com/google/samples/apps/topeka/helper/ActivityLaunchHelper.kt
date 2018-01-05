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

package com.google.samples.apps.topeka.helper

import android.app.Activity
import android.content.Intent
import android.content.Context
import android.net.Uri
import com.google.samples.apps.topeka.model.Category
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat

class ActivityLaunchHelper {

    companion object {

        const val EXTRA_EDIT = "EDIT"

        private const val URL_BASE = "https://topeka.instantappsample.com"
        private const val URL_SIGNIN = "$URL_BASE/signin"
        private const val URL_CATEGORIES = "$URL_BASE/categories"
        private const val URL_QUIZ_BASE = "$URL_BASE/quiz/"

        fun launchCategorySelection(activity: Activity, options: ActivityOptionsCompat? = null) {
            val starter = categorySelectionIntent(activity)
            if (options == null) {
                activity.startActivity(starter)
            } else {
                ActivityCompat.startActivity(activity, starter, options.toBundle())
            }
        }

        fun launchSignIn(activity: Activity, edit: Boolean = false) {
            ActivityCompat.startActivity(activity,
                    signInIntent(activity, edit),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
        }

        fun categorySelectionIntent(context: Context? = null) = baseIntent(URL_CATEGORIES, context)

        fun quizIntent(category: Category, context: Context? = null) =
                baseIntent("$URL_QUIZ_BASE${category.id}", context)

        fun signInIntent(context: Context? = null, edit: Boolean = false): Intent =
                baseIntent(URL_SIGNIN, context).putExtra(EXTRA_EDIT, edit)

        private fun baseIntent(url: String, context: Context? = null): Intent {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addCategory(Intent.CATEGORY_BROWSABLE)
            if (context != null) {
                intent.`package` = context.packageName
            }
            return intent
        }
    }
}
