
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

import com.cloudant.client.api.Database;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.model.*;

import com.cloudant.client.api.model.SearchResult;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.cloudant.config.ICloudantConnector;
import org.springframework.data.cloudant.core.mapping.event.*;
import org.springframework.data.cloudant.core.model.BaseDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by justinsaul on 6/9/15.
 */
public class CloudantTemplate<T extends BaseDocument> implements CloudantOperations<T>, ApplicationEventPublisherAware {

    private CloudantClient client;
    private final CloudantExceptionTranslator exceptionTranslator = new CloudantExceptionTranslator();
    private ApplicationEventPublisher eventPublisher;
    private Database database;
    private final Logger logger = LoggerFactory.getLogger(CloudantTemplate.class);

    public CloudantTemplate(final ICloudantConnector dbConnector){
        this.client = dbConnector.getClient();
        this.database = this.client.database(dbConnector.getDbName(), true);
    }
    public CloudantTemplate(final CloudantClient client, final String databaseName) {
        this.client = client;

        this.database = this.client.database(databaseName, true);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public T save(T obj) {

        maybeEmitEvent(new BeforeSaveEvent<T>(obj));

        Response response = database.save(obj);
        if (response != null && response.getId() != null) {
            obj.setId(response.getId());
            obj.setRevision(response.getRev());
        }

        maybeEmitEvent(new AfterSaveEvent<T>(obj));
        return obj;
    }

    @Override
    public List<T> save(List<T> batchOfObj) {
        for (T entity : batchOfObj) {
            maybeEmitEvent(new BeforeSaveEvent<T>(entity));
        }
        database.bulk(batchOfObj);
        for (T entity : batchOfObj) {
            maybeEmitEvent(new AfterSaveEvent<T>(entity));
        }
        return batchOfObj;
    }

    @Override
    public T update(T obj) {

        maybeEmitEvent(new BeforeSaveEvent<T>(obj));

        Response response = database.update(obj);
        if (response != null && response.getReason() != null) {
            obj.setRevision(response.getRev());
        }
        maybeEmitEvent(new AfterSaveEvent<T>(obj));
        return obj;
    }

    @Override
    public List<T> update(List<T> batchOfObj) {
        for (T entity : batchOfObj) {
            maybeEmitEvent(new BeforeSaveEvent<T>(entity));
        }
        database.bulk(batchOfObj);
        for (T entity : batchOfObj) {
            maybeEmitEvent(new AfterSaveEvent<T>(entity));
        }
        return batchOfObj;
    }

    @Override
    public T findById(String id, Class<T> entityClass) {
        return database.find(entityClass, id);
    }

    @Override
    public List<T> findByIndex(String index, FindByIndexOptions query, Class<T> entityClass) {
        return database.findByIndex(index, entityClass, query);
    }

    public Page<T> queryView(String view, Pageable page, boolean includeDocs, Class<T> entityClass) {
        return database.view(view).queryPage(page.getPageSize(), null, entityClass);
    }

    public List<T> queryView(String view, boolean reduce, boolean includeDocs, Class<T> entityClass) {
        return database.view(view).descending(true).reduce(reduce).includeDocs(includeDocs).query(entityClass);
    }

    // The sorting not work properly
    public List<T> queryViewAndSort(String view, boolean descending, boolean reduce, boolean includeDocs, Class<T> entityClass) {
        return database.view(view).descending(descending).reduce(reduce).includeDocs(includeDocs).query(entityClass);
    }

    public ViewResult queryView(String view, Pageable pageable, Class<T> entityClass, String key) {
        // We need a class to compose the different query conditions
        if(key == null) {
            return database.view(view).includeDocs(true)
                    .limit(pageable.getPageSize()).descending(true).skip(pageable.getOffset())
                    .queryView(String.class, Object.class, entityClass);
        }
        else {
            return database.view(view).includeDocs(true).descending(true)
                    .limit(pageable.getPageSize()).skip(pageable.getOffset())
                    .key(key)
                    .queryView(String.class, Object.class, entityClass);
        }
    }

    @Override
    public List<T> queryView(String view, boolean includeDocs, Object startKey, Object endKey, Pageable pageable, Class<T> entityClass) {
        return database.view(view).startKey(startKey).endKey(endKey).limit(pageable.getPageSize()).skip(pageable.getOffset()).includeDocs(includeDocs).query(entityClass);
    }

    public ViewResult queryViewByStartKey(String view, boolean includeDocs, Class<T> entityClass, Object[] startKey, Object[] endKey, Pageable pageable) {
        return database.view(view).startKey(startKey).endKey(endKey).limit(pageable.getPageSize()).skip(pageable.getOffset()).includeDocs(true).queryView(Object.class, Object.class, entityClass);
    }


    public SearchResult<T> search(String indexName, Integer limit, boolean includeDocs, String query, Class<T> entityClass){
        return database.search(indexName).limit(limit).includeDocs(includeDocs).querySearchResult(query, entityClass);
    }

    @Override
    public boolean exists(String id) {
        return database.contains(id);
    }

    @Override
    public void remove(T obj) {

        maybeEmitEvent(new BeforeSaveEvent<T>(obj));

        handleResponse(database.remove(obj), obj);

        maybeEmitEvent(new AfterSaveEvent<T>(obj));
    }

    @Override
    public void remove(List<T> batchOfObj) {
        for (T entity : batchOfObj) {
            maybeEmitEvent(new BeforeSaveEvent<T>(entity));
        }
        List<Map> bulkDeleteObjects = new ArrayList<Map>();
        for (T entity : batchOfObj) {
            bulkDeleteObjects.add(createDeleteDocument(entity.getId(),entity.getRevision()));
        }
        database.bulk(bulkDeleteObjects);
        for (T entity : batchOfObj) {
            maybeEmitEvent(new AfterSaveEvent<T>(entity));
        }
    }

    @Override
    public CloudantClient getCloudantClient() {
        return client;
    }

    public void setUnmappedDataAdapter() {
        setDataAdapter(null);
    }

    public void setDataAdapter(Map<Class<?>, Object> adapters) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeDataAdapter());
        gsonBuilder.registerTypeHierarchyAdapter(BaseDocument.class, new UnmappedDataAdapter());
        if(adapters != null) {
            for (Class<?> type : adapters.keySet()) {
                gsonBuilder.registerTypeAdapter(type, adapters.get(type));
            }
        }
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        this.client.setGsonBuilder(gsonBuilder);
    }

    protected <T> void maybeEmitEvent(final CloudantMappingEvent<T> event) {
        if (null != eventPublisher) {
            eventPublisher.publishEvent(event);
        }
    }

    protected Map<String, Object> createDeleteDocument(String id, String revision) {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("_id", id);
        map.put("_rev", revision);
        map.put("_deleted", true);
        return map;
    }

    protected void handleResponse(Response response, T entity) {
        if (response.getError() != null) {
            throw new RuntimeException(String.format("error %s, reason: %s, entity id: %s, rev: %s", response.getError(), response.getReason(), response.getId(), response.getRev()));
        } else if (entity != null) {
            // maybe set doc id and rev?
            entity.setRevision(response.getRev());
        }
    }


}
