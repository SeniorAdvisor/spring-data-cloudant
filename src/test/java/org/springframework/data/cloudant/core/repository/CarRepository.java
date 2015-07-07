package org.springframework.data.cloudant.core.repository;

import org.springframework.data.cloudant.core.model.Car;
import org.springframework.stereotype.Repository;

/**
 * Created by kevin on 6/16/15.
 */

@Repository
public class CarRepository extends CloudantCrudRepository<Car,String>{
    @Override
    public String defaultView() {
        return "_all_docs";
    }
}
