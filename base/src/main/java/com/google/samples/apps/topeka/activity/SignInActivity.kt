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

package com.google.samples.apps.topeka.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.google.samples.apps.topeka.base.R
import com.google.samples.apps.topeka.fragment.SignInFragment
import com.google.samples.apps.topeka.helper.ActivityLaunchHelper.Companion.EXTRA_EDIT
import com.google.samples.apps.topeka.helper.isLoggedIn
import com.google.samples.apps.topeka.helper.findFragmentById
import com.google.samples.apps.topeka.helper.replaceFragment

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if (savedInstanceState == null) {
            replaceFragment(R.id.sign_in_container,
                    SignInFragment.newInstance(isInEditMode || !isLoggedIn()))
        }
    }

    override fun onStop() {
        super.onStop()
        if (isLoggedIn()) finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Forwarding all results to SignInFragment for further handling.
        findFragmentById(R.id.sign_in_container)?.onActivityResult(requestCode, resultCode, data)
    }

    private val isInEditMode get() = intent.getBooleanExtra(EXTRA_EDIT, false)
}
