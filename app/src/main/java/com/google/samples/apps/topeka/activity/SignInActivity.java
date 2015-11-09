/*
 * Copyright 2015 Google Inc.
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

package com.google.samples.apps.topeka.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.fragment.SignInFragment;
import com.google.samples.apps.topeka.helper.PreferencesHelper;

public class SignInActivity extends AppCompatActivity {

    private static final String EXTRA_EDIT = "EDIT";

    public static void start(Activity activity, Boolean edit) {
        Intent starter = new Intent(activity, SignInActivity.class);
        starter.putExtra(EXTRA_EDIT, edit);
        //noinspection unchecked
        ActivityCompat.startActivity(activity,
                starter,
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final boolean edit = isInEditMode();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sign_in_container, SignInFragment.newInstance(edit)).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (PreferencesHelper.isSignedIn(this)) {
            finish();
        }
    }

    private boolean isInEditMode() {
        final Intent intent = getIntent();
        boolean edit = false;
        if (null != intent) {
            edit = intent.getBooleanExtra(EXTRA_EDIT, false);
        }
        return edit;
    }
}
