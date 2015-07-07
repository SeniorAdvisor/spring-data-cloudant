/*
 * Copyright 2010-2012 the original author or authors.
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

package org.springframework.data.cloudant.core;



import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.model.ConnectOptions;
import com.cloudant.client.api.Database;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.cloudant.core.model.BaseDocument;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by justinsaul on 6/9/15.
 */
public class CloudantFactoryBean implements FactoryBean<CloudantClient>, InitializingBean, PersistenceExceptionTranslator {

    //defaults here

    private CloudantClient cloudantClient;
    private PersistenceExceptionTranslator exceptionTranslator = new CloudantExceptionTranslator();
    private String username;
    private String password;
    private String account;
    private int maxConnections;
    private int connectionTimeout;
    private GsonBuilder gsonBuilder;

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setAccount(final String account) {
        this.account = account;
    }

    public void setMaxConnections(final int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setGsonBuilder(final GsonBuilder gsonBuilder) { this.gsonBuilder = gsonBuilder; }

    /**
     * Use SLF4J as the default logger if not instructed otherwise.
     */
    public static final String DEFAULT_LOGGER_PROPERTY = "net.spy.memcached.compat.log.SLF4JLogger";
    //factory builder?

    @Override
    public CloudantClient getObject() throws Exception {
        return cloudantClient;
    }

    @Override
    public Class<?> getObjectType() {
        return CloudantClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConnectOptions connectionOptions = new ConnectOptions()
                .setConnectionTimeout(connectionTimeout)
                .setMaxConnections(maxConnections);
        cloudantClient = new CloudantClient(account,username,password,connectionOptions);
        gsonBuilder = new GsonBuilder().registerTypeHierarchyAdapter(BaseDocument.class,new UnmappedDataAdapter());
        cloudantClient.setGsonBuilder(gsonBuilder);
    }

    @Override
    public DataAccessException translateExceptionIfPossible(final RuntimeException ex) {
        return  exceptionTranslator.translateExceptionIfPossible(ex);
    }
}
