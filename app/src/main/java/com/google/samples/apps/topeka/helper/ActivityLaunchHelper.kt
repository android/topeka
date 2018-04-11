package com.google.samples.apps.topeka.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import com.google.samples.apps.topeka.activity.CategorySelectionActivity
import com.google.samples.apps.topeka.activity.QuizActivity
import com.google.samples.apps.topeka.activity.SignInActivity
import com.google.samples.apps.topeka.model.Category

class ActivityLaunchHelper {

    companion object {

        const val EXTRA_EDIT = "EDIT"

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

        fun categorySelectionIntent(context: Context? = null) =
                baseIntent(CategorySelectionActivity::class.java, context)

        fun quizIntent(category: Category, context: Context? = null) =
                baseIntent(QuizActivity::class.java, context).apply {
                    putExtra(Category.TAG, category.id) }

        fun signInIntent(context: Context? = null, edit: Boolean = false): Intent =
                baseIntent(SignInActivity::class.java, context).putExtra(EXTRA_EDIT, edit)

        private fun baseIntent(activityClass: Class<out Activity>, context: Context? = null): Intent {
            return Intent(context, activityClass)
        }
    }
}