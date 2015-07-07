package org.springframework.data.cloudant.core.model;

import org.springframework.data.cloudant.core.mapping.Document;

/**
 * Created by kevin on 6/16/15.
 */

@Document
public class SimpleWithEnum extends BaseDocument {


    public enum Type {
        BIG
    }

    private Type type;

    public SimpleWithEnum(final String id, final Type type) {
        super.setId(id);
        this.type = type;
    }

   public Type getType() {
        return type;
    }

   public void setType(final Type type) {
        this.type = type;
    }
}