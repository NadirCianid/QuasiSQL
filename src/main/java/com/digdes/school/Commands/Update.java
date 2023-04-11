package com.digdes.school.Commands;

import com.digdes.school.ParsingTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Update {
    public static List<Map<String, Object>> execute(List<String> request, ParsingTest driver) throws Exception {
        int requestSize = request.size();
        if(requestSize < 3) {
            System.out.println("query is too short");
            throw  new Exception();
        }

        int whereIndex = requestSize;
        for (String word : request) {
            if(word.matches("(?i)where")) {
                whereIndex = request.indexOf(word);
            }
        }

        if(request.get(1).matches("(?i)values")) {
            List<String> values = ParsingTest.convertToUnaryWords(request.subList(2, whereIndex));
            List<Map<String, Object>> tableForUpdate;
            List<Map<String, Object>> returnTable = new ArrayList<>();

            if(whereIndex == requestSize) {
                tableForUpdate = driver.table;
            } else {
                System.out.println(request.subList(whereIndex - 1, requestSize));
                tableForUpdate = Select.execute(request.subList(whereIndex - 1 , requestSize), driver);
            }

            for (Map<String, Object> tuple: tableForUpdate) {
                returnTable.add(Insert.getTuples(values, driver.table, tuple).get(0));
            }
            return returnTable; //TODO:проверить
        }

        throw new Exception();
    }
}
