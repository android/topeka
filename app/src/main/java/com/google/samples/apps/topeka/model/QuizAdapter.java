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
package com.google.samples.apps.topeka.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.samples.apps.topeka.model.quiz.Quiz;

import java.lang.reflect.Type;

/**
 * Enables Gson deserialization of Quiz's children.
 */
public class QuizAdapter implements JsonDeserializer<Quiz> {


    @Override
    public Quiz deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (null == typeOfT || !((Class) typeOfT).isAssignableFrom(Quiz.class)) {
            return null;
        }
        JsonPrimitive typeAsPrimitive = ((JsonObject) json).getAsJsonPrimitive(JsonAttributes.TYPE);
        final String type = typeAsPrimitive.getAsString();
        return createQuizDueToType(type, json);
    }

    private Quiz createQuizDueToType(String typeName, JsonElement json) {
        for (Quiz.Type type : Quiz.Type.values()) {
            if (typeName.equals(type.getJsonName())) {
                if (null != type.getType()) {
                    return (Quiz) new Gson().fromJson(json, type.getType());
                }
            }
        }
        return null;
    }
}
