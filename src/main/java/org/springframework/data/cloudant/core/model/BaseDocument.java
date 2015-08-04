
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

package org.springframework.data.cloudant.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 6/12/15.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class BaseDocument {

    @SerializedName("_id")
    @Id
    private String id;

    @SerializedName("_rev")
    private String revision;

    //json ignore
    @JsonIgnore
    private Map<String, Object> unmappedFields;

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("_rev")
    public String getRevision() {
        return revision;
    }

    @JsonProperty("_rev")
    public void setRevision(String revision) {
        this.revision = revision;
    }

    public void setUnmappedFields(Map<String, Object> unmappedFields) {
        this.unmappedFields = unmappedFields;
    }

    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    public void addUnmappedField(String key, Object value) {
        if (unmappedFields == null) {
            unmappedFields = new HashMap<String, Object>();
        }
        unmappedFields.put(key,value);
    }
}
