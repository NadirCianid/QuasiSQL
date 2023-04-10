package com.digdes.school.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
    public static List<String> convertToUnaryWords(List<String> conditions) throws Exception {
        List<String> returnList = new ArrayList<>();
        //params of the condition
        String columnName = "";
        String operator = "";
        String param = "";//TODO: проверять параметр парама и кастить его
        String logicalOperator = "";

        int i = 0;
        while (i < conditions.size()) {
            String condition = conditions.get(i);
            if(condition.matches(",.+,")) {
                returnList.add(",");
                condition = condition.substring(1,condition.length()-1);
                conditions.add(conditions.indexOf(condition), ",");
            } else if(condition.matches(",.+")) {
                returnList.add(",");
                condition = condition.substring(1);
            } else if(condition.matches(".+,")) {
                int newPos = conditions.indexOf(condition)+1;
                condition = condition.substring(0,condition.length()-1);

                conditions.add(newPos, ",");
            } else if(condition.equals(",")) {
                returnList.add(",");
                i++;
                condition = conditions.get(i);
            }

            Pattern fullConditionPattern = Pattern.compile("(')(id|lastName|age|cost|active)(')(!=|=|>|<|>=|<=|like|ilike)([^=><]+)");
            Matcher fullConditionMatcher = fullConditionPattern.matcher(condition);

            Pattern leftConditionPattern = Pattern.compile("(')(id|lastName|age|cost|active)(')(!=|=|>|<|>=|<=|like|ilike)");
            Matcher leftConditionMatcher = leftConditionPattern.matcher(condition);

            Pattern rightConditionPattern = Pattern.compile("(!=|>=|<=|=|>|<|like|ilike)([^=><]+)");
            Matcher rightConditionMatcher = rightConditionPattern.matcher(condition);


            if (fullConditionMatcher.matches()) {
                returnList.add(fullConditionMatcher.group(2));
                returnList.add(fullConditionMatcher.group(4));
                returnList.add(fullConditionMatcher.group(5));
            } else if (leftConditionMatcher.matches()) {
                returnList.add(leftConditionMatcher.group(2));
                returnList.add(leftConditionMatcher.group(4));
            } else if (rightConditionMatcher.matches()) {
                returnList.add(rightConditionMatcher.group(1));
                returnList.add(rightConditionMatcher.group(2));
            } else if (condition.matches("(')(id|lastName|age|cost|active)(')")) {
                returnList.add(condition.substring(1, condition.length() - 1));
            } else if (condition.matches("(!=|=|>|<|>=|<=|like|ilike)")) {
                returnList.add(condition);
            } else if (returnList.size() > 0 && returnList.get(returnList.size()-1).matches("(!=|=|>|<|>=|<=|like|ilike)")) {
                returnList.add(condition);
            } else if (condition.matches("(?i)(and|or)")) {
                returnList.add(condition);
            } else {
                throw new Exception();
            }

            i++;
        }
        return returnList;
    }

}
