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
import com.google.samples.apps.topeka.model.quiz.QuizType;

import java.lang.reflect.Type;

/**
 * Allows {@link Gson} deserialization of subclasses of {@link Quiz}.
 */
public class QuizAdapter implements JsonDeserializer<Quiz> {


    @Override
    public Quiz deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // check if typeOfT is a Quiz.
        if (null == typeOfT || !((Class) typeOfT).isAssignableFrom(Quiz.class)) {
            return null;
        }
        // get the type attribute.
        JsonPrimitive typeAsPrimitive = ((JsonObject) json).getAsJsonPrimitive(JsonAttributes.TYPE);
        final String type = typeAsPrimitive.getAsString();
        // create and return the quiz.
        return createQuizDueToType(type, json);
    }

    private Quiz createQuizDueToType(String typeName, JsonElement json) {
        // iterate over available quiz types.
        for (QuizType type : QuizType.values()) {
            // check if the names match
            if (typeName.equals(type.getJsonName())) {
                // and the type is valid
                if (null != type.getType()) {
                    // deserialize the quiz.
                    return new Gson().fromJson(json, type.getType());
                }
            }
        }
        return null;
    }
}
