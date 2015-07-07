package org.springframework.data.cloudant.core.model;

import org.springframework.data.cloudant.core.mapping.Document;

/**
 * Created by kevin on 6/16/15.
 */

@Document
public class SimpleWithLong extends BaseDocument {

    private long value;

    public SimpleWithLong(final String id, final long value) {
        super.setId(id);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(final long value) {
        this.value = value;
    }
}