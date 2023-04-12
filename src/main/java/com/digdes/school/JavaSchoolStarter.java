package com.digdes.school;

import com.digdes.school.Commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaSchoolStarter {
    public List<Map<String, Object>> table = new ArrayList<>();
    public JavaSchoolStarter() {}

    public List<Map<String, Object>> execute(String request) throws Exception {
        List<String> requestWords = Arrays.stream(request.split("[ ]"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        String firstWord = requestWords.get(0);
        if (!firstWord.matches("(?i)(INSERT)|(UPDATE)|(DELETE)|(SELECT)")) {
            throw new Exception();
        }


        if (firstWord.matches("(?i)INSERT")) {
            if(requestWords.size() <= 2) {
                throw  new Exception();
            }
            return Insert.execute(requestWords, this);
        }

        if (firstWord.matches("(?i)UPDATE")) {
            return Update.execute(requestWords, this);
        }

        if (firstWord.matches("(?i)DELETE")) {
            return Delete.execute(requestWords, this);
        }

        if (firstWord.matches("(?i)SELECT")) {
            return Select.execute(requestWords, this);
        }
        return null;
    }

    public static List<String> convertToUnaryWords(List<String> conditions) throws Exception {
        List<String> returnList = new ArrayList<>();
        //params of the condition
        String columnName;
        String operator;
        String param;
        String logicalOperator;


        int i = 0;
        while (i < conditions.size()) {
            boolean comaPresented = false;// была ли запятая на данном шаге
            String condition = conditions.get(i);
            if (condition.equals(",")) {
                returnList.add(",");
                i++;
                condition = conditions.get(i);
                comaPresented = true;
            }
            if (condition.matches(",.+,")) {
                returnList.add(",");
                condition = condition.substring(1, condition.length() - 1);
                conditions.add(conditions.indexOf(condition), ",");
            } else if (condition.matches(",.+") && !comaPresented) {
                returnList.add(",");
                condition = condition.substring(1);
            } else if (condition.matches(".+,")) {
                int newPos = i + 1;
                condition = condition.substring(0, condition.length() - 1);

                conditions.add(newPos, ",");
            } else if (condition.matches(".+,.+")) {
                String newCondition =  condition.substring(condition.indexOf(',')+1);
                int newPos = conditions.indexOf(condition) + 1;
                condition = condition.substring(0, condition.indexOf(','));

                conditions.add(newPos, ",");
                conditions.add(newPos+1,newCondition);
            }
            Pattern fullConditionPattern = Pattern.compile("(')(id|lastName|age|cost|active)(')(!=|=|>|<|>=|<=|like|ilike)([^=><]+)");
            Matcher fullConditionMatcher = fullConditionPattern.matcher(condition);

            Pattern leftConditionPattern = Pattern.compile("(')(id|lastName|age|cost|active)(')(!=|=|>|<|>=|<=|like|ilike)");
            Matcher leftConditionMatcher = leftConditionPattern.matcher(condition);

            Pattern rightConditionPattern = Pattern.compile("(!=|>=|<=|=|>|<|like|ilike)([^=><]+)");
            Matcher rightConditionMatcher = rightConditionPattern.matcher(condition);

            Pattern fullLOConditionPattern = Pattern.compile("(.+)((?i)and|or)(.+)");
            Matcher fullLOConditionMatcher = fullLOConditionPattern.matcher(condition);

            Pattern rightLOConditionPattern = Pattern.compile("(.+)((?i)and|or)");
            Matcher rightLOConditionMatcher = rightLOConditionPattern.matcher(condition);

            Pattern leftLOConditionPattern = Pattern.compile("((?i)and|or)(.+)");
            Matcher leftLOConditionMatcher = leftLOConditionPattern.matcher(condition);

             if(leftLOConditionMatcher.matches()) {
                conditions.set(i, leftLOConditionMatcher.group(2));
                logicalOperator = leftLOConditionMatcher.group(1);

                returnList.add(logicalOperator);
                 i--;
            } else if(fullLOConditionMatcher.matches()) {
                 conditions.set(i, fullLOConditionMatcher.group(1));
                 logicalOperator = fullLOConditionMatcher.group(2);
                 conditions.add(i+1, logicalOperator);
                 conditions.add(i+2, fullLOConditionMatcher.group(3));
                 i--;
            } else if(rightLOConditionMatcher.matches()) {
                conditions.set(i, rightLOConditionMatcher.group(1));
                logicalOperator = rightLOConditionMatcher.group(2);

                conditions.add(i+1, logicalOperator);
                 i--;
            } else if (fullConditionMatcher.matches()) {
                columnName = fullConditionMatcher.group(2);
                returnList.add(columnName);

                operator = fullConditionMatcher.group(4);
                returnList.add(operator);

                param = fullConditionMatcher.group(5);
                returnList.add(param);
            } else if (leftConditionMatcher.matches()) {
                columnName = leftConditionMatcher.group(2);
                returnList.add(columnName);

                operator = leftConditionMatcher.group(4);
                returnList.add(operator);
            } else if (rightConditionMatcher.matches()) {
                operator = rightConditionMatcher.group(1);
                returnList.add(operator);

                param = rightConditionMatcher.group(2);
                returnList.add(param);
            } else if (condition.matches("(')(id|lastName|age|cost|active)(')")) {
                columnName = condition.substring(1, condition.length() - 1);
                returnList.add(columnName);
            } else if (condition.matches("(!=|=|>|<|>=|<=|like|ilike)")) {
                operator = condition;
                returnList.add(operator);
            } else if (returnList.size() > 0 && returnList.get(returnList.size() - 1).matches("(!=|=|>|<|>=|<=|like|ilike)")) {
                param = condition;
                returnList.add(param);
            } else if (condition.matches("(?i)(and|or)")) {
                logicalOperator = condition;
                returnList.add(logicalOperator);
            } else {

                throw new Exception();
            }
            i++;
        }
        return returnList;
    }

}
