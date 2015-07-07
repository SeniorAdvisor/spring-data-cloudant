package org.springframework.data.cloudant.core.model;

import org.springframework.data.cloudant.core.mapping.Document;

/**
 * Created by kevin on 6/16/15.
 */
@Document
public class SimpleWithClass extends BaseDocument {


private Class<Integer> integerClass;

private String value;

        SimpleWithClass(final String id, final Class<Integer> integerClass) {
        super.setId(id);
        this.integerClass = integerClass;
        }

        Class<Integer> getIntegerClass() {
        return integerClass;
        }

        void setIntegerClass(final Class<Integer> integerClass) {
        this.integerClass = integerClass;
        }

        String getValue() {
        return value;
        }

        void setValue(final String value) {
        this.value = value;
        }
        }