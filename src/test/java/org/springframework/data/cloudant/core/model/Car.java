package org.springframework.data.cloudant.core.model;


import org.springframework.data.cloudant.core.mapping.Field;

/**
 * Created by justinsaul on 6/10/15.
 */

public class Car  extends BaseDocument{


    private boolean active;
    private String name;

    @Field("is_running")
    private boolean running = true;

    public Car(String id) {
        super.setId(id);
    }

    @Override
    public String toString() {
        return "Car [id="+getId()+",name="+name+",running"+running+"]";
    }

    public Car setName(String name){
        this.name = name;
        return this;
    }

    public String getName(){
        return name;
    }

    public Car setRunning(boolean running) {
        this.running = running;
        return this;
    }

    public boolean getRunnning() {
        return running;
    }

    public boolean isActive() {
        return active;
    }

    public Car setActive(boolean active) {
        this.active = active;
        return this;
    }
}
