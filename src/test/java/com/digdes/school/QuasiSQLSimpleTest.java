package com.digdes.school;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class QuasiSQLSimpleTest {
    @Test
    public void unaryQuery() throws Exception {
        ParsingTest parser = new ParsingTest();

        List<Map<String, Object>> expectedResult = new ArrayList<>();
        assertEquals(expectedResult, parser.parseRequest("Select"));
        assertEquals(expectedResult, parser.parseRequest("delete"));
        assertEquals(expectedResult, parser.parseRequest("update values 'lastName'='Denis'"));

    }
    @Test
    public void simple() throws Exception {
        List<Map<String, Object>> expectedResult1 = new ArrayList<>();
        List<Map<String, Object>> expectedResult1_1 = new ArrayList<>();
        List<Map<String, Object>> expectedResult2 = new ArrayList<>();

        addTuple(expectedResult1, 1, "lastName", 18, 0.0, true);
        addTuple(expectedResult1_1, 3, "lastName3", 18, 2.0, true);
        addTuple(expectedResult2, 2, "lastName2", 21, 1.0, false);

        ParsingTest parser1 =  new ParsingTest();
        ParsingTest parser2 =  new ParsingTest();

        assertEquals(expectedResult1, parser1.parseRequest("insert values 'id'=1, 'lastName'='lastName', 'age'=18, 'cost' = 0.0, 'active'=true"));
        assertEquals(expectedResult1_1, parser1.parseRequest("insert values 'id'=3, 'lastName'='lastName3', 'age'=18, 'cost' = 2.0, 'active'=true"));

        parser2.parseRequest("insert values 'id'=1, 'lastName'='lastName', 'age'=18, 'cost' = 0.0, 'active'=true");
        assertEquals(expectedResult2, parser2.parseRequest("update values 'id'=2, 'lastName'='lastName2', 'age'=21, 'cost' = 1.0, 'active'=false"));

        addTuple(expectedResult1, 3, "lastName3", 18, 2.0, true);
        assertEquals(expectedResult1, parser1.parseRequest("select where 'age'=18"));

        System.out.println(parser1.table);
        assertEquals(expectedResult1_1, parser1.parseRequest("delete where 'lastName'like'l.+3'"));
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
