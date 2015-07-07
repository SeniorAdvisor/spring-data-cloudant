package org.springframework.data.cloudant.core.model;

/**
 * Created by kevin on 6/16/15.
 */

import org.springframework.data.annotation.Version;
import org.springframework.data.cloudant.core.mapping.Document;

@Document
public class VersionedClass extends BaseDocument {


    @Version
    private long version;

    private String field;

    public VersionedClass(String id, String field) {
        super.setId(id);
        this.field = field;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}