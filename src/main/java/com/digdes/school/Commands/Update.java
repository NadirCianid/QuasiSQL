package com.digdes.school.Commands;

import com.digdes.school.ParsingTest;

import java.util.List;
import java.util.Map;


public class Update {
    public static List<Map<String, Object>> execute(List<String> request, ParsingTest driver) throws Exception {
        int whereIndex = request.size();
        for (String word : request) {
            if(word.matches("(?i)where")) {
                whereIndex = request.indexOf(word);
            }
        }

        if(request.get(1).matches("(?i)values")) {
            List<String> values = ParsingTest.convertToUnaryWords(request.subList(2, whereIndex));
            List<Map<String, Object>> tableForUpdate;
            List<Map<String, Object>> returnTable = null;

            if(whereIndex == 0) {
                tableForUpdate = driver.table;
            } else {
                System.out.println(request.subList(whereIndex - 1, request.size()));
                tableForUpdate = Select.execute(request.subList(whereIndex - 1 , request.size()), driver);
            }

            for (Map<String, Object> tuple: tableForUpdate) {
                returnTable = Insert.getTuples(values, driver.table, tuple);
            }
            return returnTable;
        }

        throw new Exception();
    }
}
