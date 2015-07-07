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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
/**
 * Created by justinsaul on 6/9/15.
 */
public class CloudantMappingContext extends AbstractMappingContext<BasicCloudantPersistentEntity<?>, CloudantPersistentProperty> implements ApplicationContextAware {

    /**
     * Contains the application context to configure the application.
     */
    private ApplicationContext context;

    /**
     * The default field naming strategy.
     */
    private static final FieldNamingStrategy DEFAULT_NAMING_STRATEGY = PropertyNameFieldNamingStrategy.INSTANCE;

    /**
     * The field naming strategy to use.
     */
    private FieldNamingStrategy fieldNamingStrategy = DEFAULT_NAMING_STRATEGY;


    public void setFieldNamingStrategy(final FieldNamingStrategy fieldNamingStrategy){
        this.fieldNamingStrategy = fieldNamingStrategy == null ? DEFAULT_NAMING_STRATEGY : fieldNamingStrategy;
    }
    @Override
    protected <T> BasicCloudantPersistentEntity<?> createPersistentEntity(TypeInformation<T> typeInformation) {
        BasicCloudantPersistentEntity<T> entity = new BasicCloudantPersistentEntity<T>(typeInformation);
        if (context != null) {
            entity.setApplicationContext(context);
        }
        return entity;
    }

    @Override
    protected CloudantPersistentProperty createPersistentProperty(Field field, PropertyDescriptor descriptor, BasicCloudantPersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
            return new BasicCloudantPersistentProperty(field, descriptor, owner, simpleTypeHolder, fieldNamingStrategy);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
