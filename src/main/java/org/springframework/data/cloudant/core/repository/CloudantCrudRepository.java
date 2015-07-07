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

import com.cloudant.client.api.model.ViewResult;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.cloudant.core.CloudantTemplate;
import org.springframework.data.cloudant.core.model.BaseDocument;
import org.springframework.data.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 6/15/15.
 */


public abstract class CloudantCrudRepository<T extends BaseDocument, ID extends String> implements PagingAndSortingRepository<T, ID> {

    private final Class<T> persistentClass;

    @Autowired
    private CloudantTemplate template;

    public CloudantCrudRepository() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public <S extends T> S update(S s) {
        template.update(s);
        return s;
    }

    public T findById(ID id) {
        return findOne(id);
    }

    @Override
    public <S extends T> S save(S s) {
        template.save(s);
        return s;
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> ses) {
        return null;
    }

    @Override
    public T findOne(ID id) {
//        System.out.println(this.persistentClass.getName());
        T t = (T) template.findById(id, this.persistentClass);
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
        return template.queryView(defaultView(), false, true, this.persistentClass);
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
            template.remove(doc);
        } catch (NoDocumentException e) {

        }
    }

    @Override
    public void delete(T t) {
        template.remove(t);

    }

    @Override
    public void delete(Iterable<? extends T> ts) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Iterable<T> findAll(Sort orders) {
        return template.queryViewAndSort(this.defaultView(), false, false, true, this.persistentClass);
    }

    abstract public String defaultView();
    @Override
    public Page<T> findAll(Pageable pageable) {
        return findByView(this.defaultView(), null, pageable);
    }

    public Page<T> findByView(String view_name, String key, Pageable pageable) {
        ViewResult viewResult = template.queryView(view_name, pageable, this.persistentClass, key);
        return wrapViewResult(viewResult, pageable);
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
