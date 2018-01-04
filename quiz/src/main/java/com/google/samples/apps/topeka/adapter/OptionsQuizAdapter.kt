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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.samples.apps.topeka.quiz.R
import com.google.samples.apps.topeka.helper.inflate

/**
 * A simple adapter to display a options of a quiz.
 *
 * @param options The options to add to the adapter.
 *
 * @param layoutId Must consist of a single [TextView].
 *
 * @param context The context for the adapter.
 *
 * @param withPrefix True if a prefix should be given to all items.
 */
class OptionsQuizAdapter(
        private val options: Array<String>,
        private val layoutId: Int,
        context: Context? = null,
        withPrefix: Boolean = false
) : BaseAdapter() {

    private val alphabet: Array<String> = if (withPrefix && context != null) {
        context.resources.getStringArray(R.array.alphabet)
    } else {
        emptyArray()
    }

    override fun getCount() = options.size

    override fun getItem(position: Int) = options[position]

    override fun getItemId(position: Int) = position.toLong()

    /* Important to return true in order to get checked items from this adapter correctly */
    override fun hasStableIds(): Boolean = true

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return ((convertView ?: parent.context.inflate(layoutId, parent, false)) as TextView)
                .apply {
                    text = getText(position)
                }
    }

    private fun getText(position: Int): String {
        return if (alphabet.isNotEmpty()) "${getPrefix(position)} ${getItem(position)}"
        else getItem(position)
    }

    private fun getPrefix(position: Int): String {
        /* Only ever to be called in case alphabet != null */
        val length = alphabet.size
        if (position >= length || 0 > position) {
            throw IllegalArgumentException("Only positions between 0 and $length are supported")
        }
        return if (position < length) "${alphabet[position]}."
        else "${getPrefix(position % length)}."
    }
}
