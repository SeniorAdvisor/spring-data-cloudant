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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.lightcouch.CouchDbException;
import org.lightcouch.DocumentConflictException;
import org.lightcouch.NoDocumentException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.ClassUtils;


/**
 * Created by justinsaul on 6/9/15.
 */
public class CloudantExceptionTranslator implements PersistenceExceptionTranslator {


    /*
     * (non-Javadoc)
     * @see org.springframework.dao.support.PersistenceExceptionTranslator#translateExceptionIfPossible(java.lang.RuntimeException)
     */
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {

//        NoDocumentException(ex.getMessage(), ex);
//        DocumentConflictException(ex.getMessage(), ex);
//        CouchDbException(ex.getMessage(), ex);

        // Unable to translate exception, therefore just throw the original!
        throw ex;
    }
}
