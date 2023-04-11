package com.digdes.school.Commands;

import com.digdes.school.ParsingTest;


import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Insert extends Command{
    public static List<Map<String, Object>> execute(List<String> request, ParsingTest driver) throws Exception {
        if(request.get(1).matches("(?i)values")) {
            request = Command.convertToUnaryWords(request.subList(2, request.size()));

            Map<String, Object> newTuple = new HashMap<>();
            newTuple.put("id",null);
            newTuple.put("lastName",null);
            newTuple.put("age",null);
            newTuple.put("cost",null);
            newTuple.put("active",null);

            return getTuples(request, driver.table, newTuple);
        }

        throw new Exception();
    }

    private static List<Map<String, Object>> getTuples(List<String> conditions, List<Map<String, Object>> table, Map<String, Object> updatedTuple) throws Exception {
        //params of the condition
        String columnName;
        String operator;
        String param;

        while (conditions.size()>0) {
            columnName = conditions.remove(0);
            operator = conditions.remove(0);
            if(!operator.equals("=")) {
                System.out.println(operator);
                System.out.println("value format error (operator must be '=')");
                throw new Exception();
            }
            param = conditions.remove(0);

            insertNewValue(updatedTuple, columnName, param);
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
        table.add(updatedTuple);
        return table;
    }

    private static void insertNewValue(Map<String, Object> tuple, String columnName, String  param) throws Exception {
        switch (columnName) {
            case "id":
            case "age":
                try{
                    tuple.put(columnName, Long.parseLong(param));
                } catch (NumberFormatException longParseException) {
                    System.out.println("id|age can be only long value.");
                    throw new Exception();
                }
                break;
            case "lastName":
                if(param.matches("'.+'")) {
                    tuple.put(columnName, param.substring(1, param.length()-1));
                } else {
                    System.out.println("lastname must be in parentheses");
                    throw new Exception();
                }
                break;
            case "cost":
                try{
                    tuple.put(columnName, Double.parseDouble(param));
                } catch (NumberFormatException longParseException) {
                    System.out.println("cost can be only double value.");
                    throw new Exception();
                }
                break;
            case "active":
                if(param.matches("false|true")) {
                    tuple.put(columnName, Boolean.parseBoolean(param));
                } else {
                    System.out.println("active can be only boolean value.");
                    throw new Exception();
                }
                break;
            default:
                throw new Exception();
        }
    }
}
