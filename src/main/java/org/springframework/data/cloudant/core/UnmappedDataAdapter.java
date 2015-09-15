
/*
 *
 *  * Copyright 2010-2012 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springframework.data.cloudant.core;
import com.google.gson.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


import org.joda.time.DateTime;
import org.springframework.data.cloudant.core.model.BaseDocument;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by justinsaul on 6/12/15.
 */
public class UnmappedDataAdapter<T extends BaseDocument> implements JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(DateTime.class, new DateTimeDataAdapter())
                .create();
        T doc = gson.fromJson(json, typeOfT);
        Map<String, Object> unmapped = new HashMap<>();
        List<String> nameList = getNestedField(doc.getClass());
        JsonObject object = json.getAsJsonObject();

        //add support for annotated fields ...hack for now
        nameList.add("_id");
        nameList.add("_rev");
        nameList.add("doc_type");
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if(!nameList.contains(entry.getKey())) {
                if(entry.getValue().isJsonArray()){
                    unmapped.put(entry.getKey(),  entry.getValue().getAsJsonArray());
                }else if(entry.getValue().isJsonObject()){
                    unmapped.put(entry.getKey(),  entry.getValue().getAsJsonObject());
                }else if(entry.getValue().isJsonPrimitive()){
                    JsonPrimitive v = entry.getValue().getAsJsonPrimitive();
                    if(v.isBoolean()){
                        unmapped.put(entry.getKey(),  v.getAsBoolean());
                    }else if(v.isNumber()){
                        unmapped.put(entry.getKey(),  v.getAsNumber());
                    }else if(v.isString()){
                        unmapped.put(entry.getKey(),  v.getAsString());
                    }else if(v.isJsonNull()){
                        unmapped.put(entry.getKey(),  null);
                    }
                }else if(entry.getValue().isJsonNull()) {
                    unmapped.put(entry.getKey(), null);
                }else {
                    unmapped.put(entry.getKey(), entry.getValue().getAsString());
                }
            }

        }
        doc.setUnmappedFields(unmapped);
        return doc;
    }


    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject member = new JsonObject();
        member.addProperty("doc_type", src.getClass().getSimpleName());
        member.addProperty("_id", src.getId());
        member.addProperty("_rev", src.getRevision());

        ArrayList<Field> fields = new ArrayList();
        fields = getNestedFieldList(src.getClass(), fields);
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object fieldVal = field.get(src);
                member.add(field.getName(), context.serialize(fieldVal));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }

        Map<String, Object> unmappedProperties = src.getUnmappedFields();

        if(unmappedProperties != null) {
            for (Map.Entry<String, Object> entry : unmappedProperties.entrySet()) {
                entry.setValue(entry.getValue().toString());
                member.add(entry.getKey(), context.serialize(entry.getValue()));
            }
        }
        return member;

    }

    private List<String> getNestedField(Class<?> klass){
        return getNestedFieldList(klass, new ArrayList<>()).stream().map(Field::getName).collect(Collectors.toList());
    }

     private ArrayList<Field> getNestedFieldList(Class<?> klass, ArrayList<Field> fieldList){
        if (klass.getName() != BaseDocument.class.getName()) {
            for(Field field : klass.getDeclaredFields()) {
                fieldList.add(field);
            }
            return getNestedFieldList(klass.getSuperclass(), fieldList);
        }
        return fieldList;
    }
}

