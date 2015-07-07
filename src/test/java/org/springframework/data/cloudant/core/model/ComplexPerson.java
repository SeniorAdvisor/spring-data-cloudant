package org.springframework.data.cloudant.core.model;

import org.springframework.data.cloudant.core.mapping.Document;
import org.springframework.data.cloudant.core.mapping.Field;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 6/16/15.
 */


@Document
public class ComplexPerson extends BaseDocument {

    @Field
    private final List<String> firstnames;
    @Field
    private final List<Integer> votes;

    @Field
    private final Map<String, Boolean> info1;
    @Field
    private final Map<String, Integer> info2;

    public ComplexPerson(String id, List<String> firstnames,
                         List<Integer> votes, Map<String, Boolean> info1,
                         Map<String, Integer> info2) {
        super.setId(id);
        this.firstnames = firstnames;
        this.votes = votes;
        this.info1 = info1;
        this.info2 = info2;
    }

    public List<String> getFirstnames() {
        return firstnames;
    }

    public List<Integer> getVotes() {
        return votes;
    }

    public Map<String, Boolean> getInfo1() {
        return info1;
    }

    public Map<String, Integer> getInfo2() {
        return info2;
    }

}