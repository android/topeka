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

package com.google.samples.apps.topeka.adapter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.google.android.instantapps.InstantApps
import com.google.samples.apps.topeka.categories.R
import com.google.samples.apps.topeka.helper.ApiLevelHelper
import com.google.samples.apps.topeka.helper.database
import com.google.samples.apps.topeka.model.Category
import java.time.Instant

class CategoryAdapter(
        private val activity: Activity,
        private val onItemClickListener: AdapterView.OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val resources = activity.resources
    private val layoutInflater = LayoutInflater.from(activity)
    private var categories = activity.database().getCategories(fromDatabase = true)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(layoutInflater
                    .inflate(R.layout.item_category, parent, false) as ViewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            categories[position].run {
                setCategoryIcon(this, holder.categoryIcon)
                holder.categoryTitle.run {
                    text = name
                    if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
                        transitionName = name
                    }
                    setTextColor(getColor(theme.textPrimaryColor))
                    setBackgroundColor(getColor(theme.primaryColor))
                }
            }
            setBackgroundColor(getColor(categories[position].theme.windowBackgroundColor))
            setOnClickListener {
                onItemClickListener.onItemClick(null, it, holder.adapterPosition, holder.itemId)
            }
        }
    }

    override fun getItemId(position: Int) = categories[position].id.hashCode().toLong()

    override fun getItemCount() = categories.size

    fun getItem(position: Int): Category = categories[position]

    /**
     * @see android.support.v7.widget.RecyclerView.Adapter.notifyItemChanged
     * @param id Id of changed category.
     */
    fun notifyItemChanged(id: String) {
        updateCategories(activity)
        notifyItemChanged(getItemPositionById(id))
    }

    private fun getItemPositionById(id: String): Int =
            categories.indices.firstOrNull { categories[it].id == id } ?: -1

    private fun updateCategories(activity: Activity) {
        categories = activity.database().getCategories(fromDatabase = true)
    }

    private fun setCategoryIcon(category: Category, icon: ImageView) {
        val packageName =
                if (InstantApps.isInstantApp(activity))
                    "${activity.packageName}.categories"
                else activity.packageName

        val imageRes = resources.getIdentifier("icon_category_${category.id}",
                "drawable", packageName)
        if (category.solved) {
            icon.setImageDrawable(loadSolvedIcon(category, imageRes))
        } else {
            icon.setImageResource(imageRes)
        }
    }

    /**
     * Loads an icon that indicates that a category has already been solved.

     * @param category The solved category to display.
     *
     * @param imageRes The category's identifying image.
     *
     * @return The icon indicating that the category has been solved.
     */
    private fun loadSolvedIcon(category: Category, @DrawableRes imageRes: Int): Drawable {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            val categoryIcon = loadTintedCategoryDrawable(category, imageRes)
            val done = loadTintedDoneDrawable()
            return LayerDrawable(arrayOf(categoryIcon, done))
        }
        return loadTintedCategoryDrawable(category, imageRes)
    }

    /**
     * Loads and tints a drawable.

     * @param category The category providing the tint color
     *
     * @param imageRes The image resource to tint
     *
     * @return The tinted resource
     */
    private fun loadTintedCategoryDrawable(category: Category, @DrawableRes imageRes: Int) =
            getTintedDrawable(imageRes, category.theme.primaryColor)

    /**
     * Loads and tints a check mark.

     * @return The tinted check mark
     */
    private fun loadTintedDoneDrawable() = getTintedDrawable(
            com.google.samples.apps.topeka.base.R.drawable.ic_tick)

    private fun getTintedDrawable(@DrawableRes imageRes: Int,
                                  @ColorRes tintColorRes: Int = android.R.color.white
    ): Drawable {
        return ContextCompat.getDrawable(activity, imageRes)!!.mutate().apply {
            wrapAndTint(this, tintColorRes)
        }
    }

    private fun wrapAndTint(drawable: Drawable, @ColorRes color: Int) =
            DrawableCompat.wrap(drawable)
                    .apply { DrawableCompat.setTint(this, getColor(color)) }

    /**
     * Convenience method for color loading.

     * @param colorRes The resource id of the color to load.
     *
     * @return The loaded color.
     */
    private fun getColor(@ColorRes colorRes: Int) = ContextCompat.getColor(activity, colorRes)

    class ViewHolder(categoryView: ViewGroup) : RecyclerView.ViewHolder(categoryView) {
        val categoryIcon: ImageView = categoryView.findViewById(R.id.category_icon)
        val categoryTitle: TextView = categoryView.findViewById(R.id.category_title)
    }

}
