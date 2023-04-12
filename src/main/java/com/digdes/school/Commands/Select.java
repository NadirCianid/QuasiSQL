package com.digdes.school.Commands;

import com.digdes.school.JavaSchoolStarter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Select {
    public static List<Map<String, Object>> execute(List<String> request, JavaSchoolStarter driver) throws Exception {

        int requestSize = request.size();
        if(requestSize == 1) {
            return new ArrayList<>(driver.table);
        }

        if(request.get(1).matches("(?i)WHERE")) {
            request = JavaSchoolStarter.convertToUnaryWords(request.subList(2, requestSize));
            return getTuples(request, driver.table);
        }

        throw new Exception();
    }

    private static List<Map<String, Object>> getTuples(List<String> conditions, List<Map<String, Object>> table) throws Exception {
        List<Map<String, Object>> resultTable = new ArrayList<>();
        List<String> whereResult;
        //params of the condition
        String columnName;
        String operator;
        String param;

        for (Map<String, Object> tuple: table) {
            whereResult = new ArrayList<>();
            int i = 0;
            boolean lo = true; // флаг, показывающий был ли логический оператор на предыдущем шаге
            while (i < conditions.size()) {
                if (!conditions.get(i).matches("(?i)and|or") && lo && i + 2 <= conditions.size() - 1) {
                    columnName = conditions.get(i);
                    operator = conditions.get(i + 1);
                    param = conditions.get(i + 2);

                    if (columnName.equals("lastName")) {
                        if (!param.matches("'[^=><]+'")) {
                            throw new Exception();
                        }
                        param = param.substring(1, param.length() - 1);
                    }
                    whereResult.add(String.valueOf(validateTuple(tuple, columnName, operator, param)));
                    i += 3;
                    lo = false;
                } else if(conditions.get(i).matches("(?i)and|or") && i < conditions.size()-3 && !lo){
                    lo = true;
                    whereResult.add(conditions.get(i).toLowerCase());
                    i++;
                } else {
                    throw new Exception();
                }
            }
            processLogicalOperators(whereResult, "and");
            processLogicalOperators(whereResult, "or");

            if(Boolean.parseBoolean(whereResult.remove(0))) {
                resultTable.add(tuple);
            }
        }

        return resultTable;
    }

    private static void processLogicalOperators(List<String> whereResult, String logicalOperator) {
        boolean logicalOperatorResult;
        for (int j = 0; j < whereResult.size(); j++) {
            if(whereResult.get(j).equals(logicalOperator)) {
                if(logicalOperator.equals("and")) {
                    logicalOperatorResult = Boolean.parseBoolean(whereResult.get(j - 1)) && Boolean.parseBoolean(whereResult.get(j + 1));
                    whereResult.set(j + 1, String.valueOf(logicalOperatorResult));
                    whereResult.remove(j - 1);
                    whereResult.remove(j - 1);
                }
                if(logicalOperator.equals("or") && whereResult.size() > 1) {
                    logicalOperatorResult = Boolean.parseBoolean(whereResult.get(j - 1)) || Boolean.parseBoolean(whereResult.get(j + 1));
                    whereResult.set(j + 1, String.valueOf(logicalOperatorResult));
                    whereResult.remove(0);
                    whereResult.remove(0);
                }
            }
        }
    }

    private static boolean validateTuple(Map<String, Object> tuple,String columnName, String operator, String param) throws Exception {
            if(tuple.isEmpty() || tuple.get(columnName) == null) {
                return false;
            }

            String attribute = tuple.get(columnName).toString();

            switch (operator) {
                case "=":
                    if(attribute.equals(param)) {
                        return true;
                    }
                    break;

                case "!=":
                    if(!attribute.equals(param)) {
                        return true;
                    }
                    break;

                case ">":
                    try{
                        if(Long.parseLong(attribute) > Long.parseLong(param)) {
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if(Double.parseDouble(attribute) > Double.parseDouble(param)) {
                                return true;
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case ">=":
                    try{
                        if(Long.parseLong(attribute) >= Long.parseLong(param)) {
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if(Double.parseDouble(attribute) >= Double.parseDouble(param)) {
                                return true;
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case "<":
                    try{
                        if(Long.parseLong(attribute) < Long.parseLong(param)) {
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if (Double.parseDouble(attribute) < Double.parseDouble(param)) {
                                return true;
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case "<=":
                    try{
                        if(Long.parseLong(attribute) <= Long.parseLong(param)) {
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        try {
                            if(Double.parseDouble(attribute) <= Double.parseDouble(param)) {
                                return true;
                            }
                        } catch (NumberFormatException ex) {
                            throw new Exception();
                        }
                    }
                    break;

                case "like":
                    if(attribute.matches(param)) {
                        return true;
                    }
                    break;

                case "ilike":
                    if(attribute.toLowerCase().matches(param.toLowerCase())) {
                        return true;
                    }
                    break;

                default:
                    throw new Exception();
            }
        return false;
    }
}
