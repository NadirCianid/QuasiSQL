package com.digdes.school;

import java.util.*;
import java.util.stream.Collectors;


public class Insert{
    private static boolean idValuePresented = false;
    private static boolean lastNameValuePresented = false;
    private static boolean ageValuePresented = false;
    private static boolean costValuePresented = false;
    private static boolean activeValuePresented = false;

    public static List<Map<String, Object>> execute(List<String> request, JavaSchoolStarter driver) throws Exception {
        if(request.get(1).matches("(?i)values")) {
            request = JavaSchoolStarter.convertToUnaryWords(request.subList(2, request.size()));
            Map<String, Object> newTuple = new HashMap<>();
            newTuple.put("id",null);
            newTuple.put("lastName",null);
            newTuple.put("age",null);
            newTuple.put("cost",null);
            newTuple.put("active",null);

            return getTuples(request, driver.table, newTuple);
        }
        System.out.println("VALUES expected");
        throw new Exception();
    }

    public static List<Map<String, Object>> getTuples(List<String> conditions, List<Map<String, Object>> table, Map<String, Object> updatedTuple) throws Exception {
        Map<String, Object> tupleForInsert = new HashMap<>(updatedTuple);
        //params of the condition
        String columnName;
        String operator;
        String param;

        while (conditions.size()>0) {
            columnName = conditions.remove(0);
            operator = conditions.remove(0);
            if(!operator.equals("=")) {
                System.out.println("found operator" + operator);
                System.out.println("value format error (operator must be '=')");
                throw new Exception();
            }
            param = conditions.remove(0);

            insertNewValue(tupleForInsert, columnName, param);
            if(conditions.size() == 0) {
                break;
            }

            String condition = conditions.get(0);
            if(condition.equals(",")) {
                conditions.remove(0);
            } else {
                System.out.println("after value must be comma");
                throw new Exception();
            }
        }
        if(table.contains(updatedTuple)) {
            table.set(table.indexOf(updatedTuple), tupleForInsert);
        } else {
            table.add(tupleForInsert);
        }

        updatedTuple = table.subList(table.size()-1, table.size()).stream()
                .flatMap(tuple -> tuple.entrySet().stream())
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new));
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(updatedTuple);


        idValuePresented = false;
        lastNameValuePresented = false;
        ageValuePresented = false;
        costValuePresented = false;
        activeValuePresented = false;

        return result;
    }

    private static void insertNewValue(Map<String, Object> tuple, String columnName, String  param) throws Exception {
        if(param.equals("null")) {
            tuple.put(columnName, null);
            return;
        }

        switch (columnName) {
            case "id":
            case "age":
                if(columnName.equals("id") && idValuePresented || columnName.equals("age") && ageValuePresented) {
                    System.out.println("repeating value");
                    throw new Exception();
                }

                if(columnName.equals("id")) {
                    idValuePresented = true;
                } else {
                    ageValuePresented = true;
                }

                try{
                    tuple.put(columnName, Long.parseLong(param));
                } catch (NumberFormatException longParseException) {
                    System.out.println("id|age can be only long value.");
                    throw new Exception();
                }
                break;
            case "lastName":
                if(lastNameValuePresented) {
                    System.out.println("repeating value");
                    throw new Exception();
                }
                lastNameValuePresented = true;
                if(param.matches("'.+'")) {
                    tuple.put(columnName, param.substring(1, param.length()-1));
                } else {
                    System.out.println("lastname must be in parentheses");
                    throw new Exception();
                }
                break;
            case "cost":
                if(costValuePresented) {
                    System.out.println("repeating value");
                    throw new Exception();
                }
                costValuePresented = true;
                try{
                    tuple.put(columnName, Double.parseDouble(param));
                } catch (NumberFormatException longParseException) {
                    System.out.println("cost can be only double value.");
                    throw new Exception();
                }
                break;
            case "active":
                if(activeValuePresented) {
                    System.out.println("repeating value");
                    throw new Exception();
                }
                activeValuePresented = true;
                if(param.matches("false|true")) {
                    tuple.put(columnName, Boolean.parseBoolean(param));
                } else {
                    System.out.println("active can be only boolean value.");
                    throw new Exception();
                }
                break;
            default:
                System.out.println("unexpected symbols");
                throw new Exception();
        }
    }
}
