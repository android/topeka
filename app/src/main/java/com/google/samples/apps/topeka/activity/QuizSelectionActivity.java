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

package com.google.samples.apps.topeka.activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.samples.apps.topeka.fragment.CategoryGridFragment;
import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Player;
import com.google.samples.apps.topeka.model.QuizAdapter;
import com.google.samples.apps.topeka.model.quiz.abstracts.Quiz;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

public class QuizSelectionActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private static final String URL
            = "http://www.polymer-project.org/apps/topeka/components/topeka-elements/categories.json";
    private static final String EXTRA_PLAYER = "player";

    public static void start(Context context, Player player) {
        Intent starter = new Intent(context, QuizSelectionActivity.class);
        starter.putExtra(EXTRA_PLAYER, player);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selection);
        Player player = getIntent().getParcelableExtra(EXTRA_PLAYER);
        setUpToolbar(player, (Toolbar) findViewById(R.id.toolbar_player));
        if (savedInstanceState == null) {
            loadCategories();
        }
    }

    private void setUpToolbar(Player player, Toolbar toolbar) {
        toolbar.setTitle(getDisplayName(player));
        //TODO fix navigation icon size
        toolbar.setNavigationIcon(player.getAvatar().getDrawableId());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationContentDescription(getString(R.string.description_user_preferences));
    }

    private String getDisplayName(Player player) {
        return getString(R.string.player_display_name, player.getFirstName(),
                player.getLastInitial());
    }

    private void loadCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        Log.d(TAG, "Array: " + array.length());
                        Category[] categories = getCustomizedGson()
                                .fromJson(array.toString(), Category[].class);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.quiz_container,
                                        CategoryGridFragment.newInstance(categories)).commit();
                        setProgressBarVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //FIXME handle error responses
                    }
                }));
    }

    private Gson getCustomizedGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Quiz.class, new QuizAdapter());
        return gsonBuilder.create();
    }

    private void setProgressBarVisibility(int visibility) {
        findViewById(R.id.progress).setVisibility(visibility);
    }
}

