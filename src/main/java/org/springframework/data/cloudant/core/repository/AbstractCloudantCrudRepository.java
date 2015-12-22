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

package org.springframework.data.cloudant.core.repository;

import com.cloudant.client.api.model.SearchResult;
import com.cloudant.client.api.model.ViewResult;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cloudant.core.CloudantTemplate;
import org.springframework.data.cloudant.core.model.BaseDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kevin on 6/15/15.
 */


public abstract class AbstractCloudantCrudRepository<T extends BaseDocument, ID extends String> implements PagingAndSortingRepository<T, ID> {

    private final Class<T> persistentClass;


    public abstract CloudantTemplate getTemplate();

    public AbstractCloudantCrudRepository() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public <S extends T> S update(S s) {
        getTemplate().update(s);
        return s;
    }

    public T findById(ID id) {
        return findOne(id);
    }

    @Override
    public <S extends T> S save(S s) {
        getTemplate().save(s);
        return s;
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> ses) {
        ArrayList<S> list = new ArrayList<S>();
        if (ses != null) {
            for (S e : ses) {
                list.add(e);
            }
        }
        return getTemplate().save(list);
    }

    @Override
    public T findOne(ID id) {
        T t = (T) getTemplate().findById(id, this.persistentClass);
        return t;
    }

    @Override
    public boolean exists(ID id) {
        try {
            findOne(id);
            return true;
        } catch (NoDocumentException e) {
            return false;
        }
    }

    @Override
    public Iterable<T> findAll() {
        return getTemplate().queryView(defaultView(), false, true, this.persistentClass);
    }

    @Override
    public Iterable<T> findAll(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(ID id) {
        try {
            BaseDocument doc = findOne(id);
            getTemplate().remove(doc);
        } catch (NoDocumentException e) {

        }
    }

    @Override
    public void delete(T t) {
        getTemplate().remove(t);

    }

    @Override
    public void delete(Iterable<? extends T> ts) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Iterable<T> findAll(Sort orders) {
        return getTemplate().queryViewAndSort(this.defaultView(), false, false, true, this.persistentClass);
    }

    abstract public String defaultView();

    @Override
    public Page<T> findAll(Pageable pageable) {
        return findByView(this.defaultView(), null, pageable);
    }

    public Page<T> findByView(String view_name, String key, Pageable pageable) {
        ViewResult viewResult = getTemplate().queryView(view_name, pageable, this.persistentClass, key);
        return wrapViewResult(viewResult, pageable);
    }

    public Iterable<T> findByView(String view_name, List<String> keys, boolean descending, boolean reduce, boolean includeDocs) {
        return getTemplate().findByKeys(view_name, keys, descending, reduce, includeDocs, this.persistentClass);
    }

    public Iterable<T> findByView(String view_name, Object startKey, Object endKey, Pageable pageable) {
        return getTemplate().queryView(view_name, true, startKey, endKey, pageable, this.persistentClass);
    }

    public SearchResult<T> search(String indexName, Integer limit, boolean includDocs, String query) {
        return getTemplate().search(indexName, limit, includDocs, query, this.persistentClass);
    }

    public Page<T> queryViewByComplexKey(String view_name, Object[] startKey, Object[] endKey, Pageable pageable) {
        List<T> result = new ArrayList<T>();
        ViewResult viewResult = getTemplate().queryViewByStartKey(view_name, true, this.persistentClass, startKey, endKey, pageable);
        List<ViewResult<String, String, T>.Rows> rows = viewResult.getRows();
        for (int i = 0; i < rows.size(); i++) {
            T item = rows.get(i).getDoc();
            if (item != null) {
                item.getUnmappedFields().put("key", rows.get(i).getKey()); // For query from views
                item.getUnmappedFields().put("value", rows.get(i).getValue());
                result.add(item);
            }
        }
        return new PageImpl<T>(result, pageable, viewResult.getTotalRows());
    }

    private PageImpl<T> wrapViewResult(ViewResult viewResult, Pageable pageable) {
        List<T> result = new ArrayList<T>();
        List<ViewResult<String, String, T>.Rows> rows = viewResult.getRows();
        for (int i = 0; i < rows.size(); i++) {
            result.add(rows.get(i).getDoc());
        }

        PageImpl<T> r = new PageImpl<T>(result, pageable, viewResult.getTotalRows());
        return r;
    }
}
