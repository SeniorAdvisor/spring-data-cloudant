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

package org.springframework.data.cloudant.core.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by justinsaul on 6/9/15.
 */
public class BasicCloudantPersistentProperty extends AnnotationBasedPersistentProperty<CloudantPersistentProperty> implements CloudantPersistentProperty {

    private static final Logger LOG = LoggerFactory.getLogger(BasicCloudantPersistentProperty.class);
    private static final String ID_FIELD_NAME = "_id";
    private static final String LANGUAGE_FIELD_NAME = "language";
    private static final Set<Class<?>> SUPPORTED_ID_TYPES = new HashSet<Class<?>>();
    private static final Set<String> SUPPORTED_ID_PROPERTY_NAMES = new HashSet<String>();

    static {

        SUPPORTED_ID_TYPES.add(UUID.class);
        SUPPORTED_ID_TYPES.add(String.class);
        SUPPORTED_ID_TYPES.add(BigInteger.class);

        SUPPORTED_ID_PROPERTY_NAMES.add("id");
        SUPPORTED_ID_PROPERTY_NAMES.add("_id");
    }
    private final FieldNamingStrategy fieldNamingStrategy;

    public BasicCloudantPersistentProperty(Field field, PropertyDescriptor propertyDescriptor, PersistentEntity<?, CloudantPersistentProperty> owner, SimpleTypeHolder simpleTypeHolder, FieldNamingStrategy fieldNamingStrategy) {
        super(field, propertyDescriptor, owner, simpleTypeHolder);
        this.fieldNamingStrategy = fieldNamingStrategy == null ? PropertyNameFieldNamingStrategy.INSTANCE : fieldNamingStrategy;

        if (isIdProperty() && getFieldName() != ID_FIELD_NAME) {
            LOG.warn("Custom field name for id property is not supported!");
        }
    }

    @Override
    protected Association<CloudantPersistentProperty> createAssociation() {
        return new Association<CloudantPersistentProperty>(this, null);
    }

    @Override
    public String getFieldName() {
//        org.springframework.data.cloudant.core.mapping.Field annotation = getField().
//                getAnnotation(org.springframework.data.cloudant.core.mapping.Field.class);
//
//        if (annotation != null && StringUtils.hasText(annotation.value())) {
//            return annotation.value();
//        }
//
//        String fieldName = fieldNamingStrategy.getFieldName(this);
//
//        if (!StringUtils.hasText(fieldName)) {
//            throw new MappingException(String.format("Invalid (null or empty) field name returned for property %s by %s!",
//                    this, fieldNamingStrategy.getClass()));
//        }
//
//        return fieldName;

        if (isIdProperty()) {

            if (owner == null) {
                return ID_FIELD_NAME;
            }

            if (owner.getIdProperty() == null) {
                return ID_FIELD_NAME;
            }

            if (owner.isIdProperty(this)) {
                return ID_FIELD_NAME;
            }
        }

        if (hasExplicitFieldName()) {
            return getAnnotatedFieldName();
        }

        String fieldName = fieldNamingStrategy.getFieldName(this);

        if (!StringUtils.hasText(fieldName)) {
            throw new MappingException(String.format("Invalid (null or empty) field name returned for property %s by %s!",
                    this, fieldNamingStrategy.getClass()));
        }

        return fieldName;
    }

    @Override
    public boolean isIdProperty() {
        if(super.isIdProperty()){
            return true;
        }

        return SUPPORTED_ID_PROPERTY_NAMES.contains(getName()) && !hasExplicitFieldName();
    }

    private boolean hasExplicitFieldName() {
        return StringUtils.hasText(getAnnotatedFieldName());
    }

    private String getAnnotatedFieldName() {
        org.springframework.data.cloudant.core.mapping.Field annotation = findAnnotation(org.springframework.data.cloudant.core.mapping.Field.class);

        if (annotation != null && StringUtils.hasText(annotation.value())) {
            return annotation.value();
        }

        return null;
    }



    @Override
    public boolean isExplicitIdProperty() {
        return isAnnotationPresent(Id.class);
    }
}
