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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.google.samples.apps.topeka.base.R
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.widget.AvatarView

/**
 * Adapter to display [Avatar] icons.
 */
class AvatarAdapter(context: Context) : BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return ((convertView ?:
                layoutInflater.inflate(R.layout.item_avatar, parent, false)) as AvatarView)
                .also { setAvatar(it, avatars[position]) }
    }

    private fun setAvatar(view: AvatarView, avatar: Avatar) {
        with(view) {
            setAvatar(avatar.drawableId)
            contentDescription = avatar.nameForAccessibility
        }
    }

    override fun getCount() = avatars.size

    override fun getItem(position: Int) = avatars[position]

    override fun getItemId(position: Int) = position.toLong()

    companion object {

        private val avatars = Avatar.values()
    }
}
