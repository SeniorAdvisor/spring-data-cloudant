package org.springframework.data.cloudant.core.repository;

import org.springframework.data.cloudant.core.model.SimplePerson;
import org.springframework.stereotype.Repository;

/**
 * Created by kevin on 6/16/15.
 */

@Repository
public class SimplePersonRepository extends CloudantCrudRepository<SimplePerson,String>{
    @Override
    public String defaultView() {
        return "_all_docs";
    }
}
