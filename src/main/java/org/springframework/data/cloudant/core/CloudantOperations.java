
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

import java.util.List;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.View;
import com.cloudant.client.api.model.FindByIndexOptions;
import com.cloudant.client.api.model.ViewResult;
import org.springframework.data.cloudant.core.model.BaseDocument;
import org.springframework.data.domain.Pageable;

/**
 * Created by justinsaul on 6/9/15.
 */
public interface CloudantOperations<T extends BaseDocument> {

    T save(T obj);

    List<T> save(List<T> batchOfObj);

    T update(T obj);

    List<T> update(List<T> batchOfObj);

    T findById(String id, Class<T> entityClass);

    List<T> findByIndex(String index, FindByIndexOptions query, Class<T> entityClass);

    List<T> queryView(String view, boolean includeDocs, Object startKey, Object endKey, Pageable pageable, Class<T> entityClass);

    boolean exists(String id);

    void remove(T obj);

    void remove(List<T> batchOfObj);

    CloudantClient getCloudantClient();

}
