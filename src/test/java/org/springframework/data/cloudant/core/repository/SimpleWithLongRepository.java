package org.springframework.data.cloudant.core.repository;

import org.springframework.data.cloudant.core.model.SimplePerson;
import org.springframework.data.cloudant.core.model.SimpleWithLong;
import org.springframework.stereotype.Repository;

/**
 * Created by kevin on 6/16/15.
 */

@Repository
public class SimpleWithLongRepository extends CloudantCrudRepository<SimpleWithLong,String>{
    @Override
    public String defaultView() {
        return "_all_docs";
    }
}
