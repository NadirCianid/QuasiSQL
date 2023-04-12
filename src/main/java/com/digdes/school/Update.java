package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Update {
    public static List<Map<String, Object>> execute(List<String> request, JavaSchoolStarter driver) throws Exception {
        int requestSize = request.size();
        if(requestSize < 3) {
            System.out.println("query is too short");
            throw  new Exception();
        }

        int whereIndex = requestSize;
        for (String word : request) {
            if (word.matches("(?i)where")) {
                whereIndex = request.indexOf(word);
                break;
            }
        }

        if(request.get(1).matches("(?i)values")) {
            List<String> values = JavaSchoolStarter.convertToUnaryWords(request.subList(2, whereIndex));
            List<Map<String, Object>> tableForUpdate;
            List<Map<String, Object>> returnTable = new ArrayList<>();

            if(whereIndex == requestSize) {
                tableForUpdate = driver.table;
            } else {
                tableForUpdate = Select.execute(request.subList(whereIndex , request.size()), driver);
            }

            for (Map<String, Object> tuple: tableForUpdate) {
                returnTable.add(Insert.getTuples(values, driver.table, tuple).get(0));
            }
            return returnTable;
        }

        System.out.println("values expected");
        throw new Exception();
    }
}
