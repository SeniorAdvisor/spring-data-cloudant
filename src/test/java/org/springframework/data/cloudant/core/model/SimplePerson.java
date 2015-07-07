package org.springframework.data.cloudant.core.model;

import org.springframework.data.cloudant.core.mapping.Document;
import org.springframework.data.cloudant.core.mapping.Field;

/**
 * Created by kevin on 6/16/15.
 */
@Document
public class SimplePerson extends BaseDocument {


    @Field
    private final String name;

    public SimplePerson(String id, String name) {
        super.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}