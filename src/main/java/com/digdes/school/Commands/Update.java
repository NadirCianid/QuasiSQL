package com.digdes.school.Commands;

import com.digdes.school.ParsingTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Update extends Command{
    public static List<Map<String, Object>> execute(List<String> request, ParsingTest driver) throws Exception {
        if(request.get(1).matches("(?i)values")) {
            return getTuples(request.subList(2, request.size()), driver.table);
        }

        throw new Exception();
    }

    private static List<Map<String, Object>> getTuples(List<String> conditions, List<Map<String, Object>> table) throws Exception {
        //params of the condition
        String columnName = "";
        String operator = "";
        String param = "";

        int i = 0;
        while (i < conditions.size()) {
            String condition = conditions.get(i);

            Pattern fullConditionPattern = Pattern.compile("(')(id|lastName|age|cost|active)(')(!=|=|>|<|>=|<=|like|ilike)(.+)");
            Matcher fullConditionMatcher = fullConditionPattern.matcher(condition);

            Pattern leftConditionPattern = Pattern.compile("(')(id|lastName|age|cost|active)(')(!=|=|>|<|>=|<=|like|ilike)");
            Matcher leftConditionMatcher = leftConditionPattern.matcher(condition);

            Pattern rightConditionPattern = Pattern.compile("(!=|=|>|<|>=|<=|like|ilike)(.+)");
            Matcher rightConditionMatcher = rightConditionPattern.matcher(condition);


            if(fullConditionMatcher.matches()) {
                columnName =  fullConditionMatcher.group(2);
                operator = fullConditionMatcher.group(4);
                param = fullConditionMatcher.group(5);
            } else if(leftConditionMatcher.matches()) {
                columnName =  leftConditionMatcher.group(2);
                operator = leftConditionMatcher.group(4);
            } else if(rightConditionMatcher.matches()) {
                operator = rightConditionMatcher.group(1);
                param = rightConditionMatcher.group(2);
            } else if(condition.matches("(')(id|lastName|age|cost|active)(')")) {
                columnName = condition.substring(1, condition.length()-1);
            } else if (condition.matches("(!=|=|>|<|>=|<=|like|ilike)")) {
                operator = condition;
            } else if(!operator.isEmpty() && !columnName.isEmpty()){
                param = condition;
            } else {
                throw new Exception();
            }

            if(!columnName.isEmpty() && !operator.isEmpty() && !param.isEmpty()) {  //TODO: для инсерта регекс оператора сделать просто "=" (мб передавать этот регекс парамтером в будущий метод)
                String finalColumnName = columnName;
                String finalParam = param;
                for (Map<String, Object> map : table) {
                    insertNewValue(map, finalColumnName, finalParam);
                }
                columnName = "";
                operator = "";
                param = "";
            }

            i++;
        }
        return table;
    }

    private static void insertNewValue(Map<String, Object> tuple, String columnName, String param) throws Exception {
        try{
            tuple.put(columnName, Long.parseLong(param));
        } catch (NumberFormatException longParseException) {
            try {
                tuple.put(columnName, Double.parseDouble(param));
            }
            catch (NumberFormatException doubleParseException) {
                if(param.matches("false|true")) {
                    tuple.put(columnName, Boolean.parseBoolean(param));
                } else if(param.matches("'.+'")) {
                    tuple.put(columnName, param.substring(1, param.length()-1));
                } else {
                    throw new Exception();
                }

            }
        }

    }
}
