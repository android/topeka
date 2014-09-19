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

package com.google.samples.apps.topeka;

import com.google.gson.Gson;
import com.google.samples.apps.topeka.model.Category;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private static final String URL
            = "http://www.polymer-project.org/apps/topeka/components/topeka-elements/categories.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            loadCategories();
            if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT) {
                getWindow().setNavigationBarColor(
                        getResources().getColor(R.color.topeka_primary_dark));
            }
        }
    }

    private void loadCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        Log.d(TAG, "Array: " + array.length());
                        Category[] categories = new Gson()
                                .fromJson(array.toString(), Category[].class);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.main_activity_container,
                                        CategoryGridFragment.newInstance(categories)).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //FIXME handle error responses
                    }
                }));
    }

}

