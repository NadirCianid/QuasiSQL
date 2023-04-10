package com.digdes.school.Commands;

import com.digdes.school.JavaSchoolStarter;
import com.digdes.school.ParsingTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Select extends Command{
    public static List<Map<String, Object>> execute(List<String> request, ParsingTest driver) throws Exception {

        if(request.size() == 1) {
            return driver.table;
        }

        if(request.get(1).matches("(?i)WHERE")) {
            request = Command.convertToUnaryWords(request.subList(2, request.size()));
            if(request.size() < 3 || (request.size() - 3)%4 != 0) {
                throw new Exception();
            }
            return getTuples(request, driver.table);
        }

        throw new Exception();
    }

    private static List<Map<String, Object>> getTuples(List<String> conditions, List<Map<String, Object>> table) throws Exception {
        List<Map<String, Object>> resultTable = new ArrayList<>();
        //params of the condition
        String columnName = "";
        String operator = "";
        String param = "";
        String logicalOperator = "";

        while (!conditions.isEmpty()) {
            boolean severalConditions = conditions.size() > 3 && (conditions.size() - 3) % 4 == 0;
            columnName = conditions.remove(0);
            operator = conditions.remove(0);
            param = conditions.remove(0);


            if(logicalOperator.isEmpty() || logicalOperator.matches("(?i)or")) { //TODO: добавить приоритет
                selectTuples(table, resultTable, columnName, operator, param);
            } else if(logicalOperator.matches("(?i)and")) {
                List<Map<String, Object>> localResultTable = new ArrayList<>();
                selectTuples(resultTable, localResultTable,  columnName, operator, param);
                resultTable = localResultTable;
            }
            if(severalConditions) {
                logicalOperator = conditions.remove(0);
            } else {
                logicalOperator = "";
            }
        }

        return resultTable;
    }

    private static void selectTuples(List<Map<String, Object>> table, List<Map<String, Object>> resultTable, String columnName, String operator, String param) throws Exception {
        for (Map<String, Object> map: table) {
            if(map.isEmpty()) {
                return;
            }

            if(map.get(columnName) == null || resultTable.contains(map)) {
               continue;
            }
            String attribute = map.get(columnName).toString();

            switch (operator) {
                case "=":
                    if(attribute.equals(param)) {
                        resultTable.add(map);
                    }
                    break;

                case "!=":
                    if(!attribute.equals(param)) {
                        resultTable.add(map);
                    }
                    break;

                case ">":
                    try{
                        if(Long.parseLong(attribute) > Long.parseLong(param)) {
                            resultTable.add(map);
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if(Double.parseDouble(attribute) > Double.parseDouble(param)) {
                                resultTable.add(map);
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case ">=":
                    try{
                        if(Long.parseLong(attribute) >= Long.parseLong(param)) {
                            resultTable.add(map);
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if(Double.parseDouble(attribute) >= Double.parseDouble(param)) {
                                resultTable.add(map);
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case "<":
                    try{
                        if(Long.parseLong(attribute) < Long.parseLong(param)) {
                            resultTable.add(map);
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if (Double.parseDouble(attribute) < Double.parseDouble(param)) {
                                resultTable.add(map);
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case "<=":
                    try{
                        if(Long.parseLong(attribute) <= Long.parseLong(param)) {
                            resultTable.add(map);
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if(Double.parseDouble(attribute) <= Double.parseDouble(param)) {
                                resultTable.add(map);
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case "like":
                    if(attribute.matches(param)) {
                        resultTable.add(map);
                    }
                    break;

                case "ilike":
                    if(attribute.matches("(?i)" + param)) {
                        resultTable.add(map);
                    }
                    break;

                default:
                    throw new Exception();
            }
        }
    }
}
