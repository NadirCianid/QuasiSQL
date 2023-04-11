package com.digdes.school;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class QuasiSQLSimpleTest {
    @Test
    public void simple() throws Exception {
        List<Map<String, Object>> expectedResult = new ArrayList<>();
        addTuple(expectedResult, 1, "lastName", 18, 0.0, true);

        ParsingTest parser =  new ParsingTest();

        assertEquals(expectedResult, parser.parseRequest("insert values 'id'=1, 'lastName'='lastName', 'age'=18, 'cost' = 0.0, 'active'=true"));

    }

    private void addTuple(List<Map<String, Object>> table, long id, String lastName,long age, Double cost, boolean active) {
        Map<String, Object> newTuple = new HashMap<>();
        newTuple.put("id",id);
        newTuple.put("lastName", lastName);
        newTuple.put("age",age);
        newTuple.put("cost",cost);
        newTuple.put("active",active);

        table.add(newTuple);
    }
}
