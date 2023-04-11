package com.digdes.school.Commands;

import com.digdes.school.ParsingTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Delete {
    public static List<Map<String, Object>> execute(List<String> request, ParsingTest driver) throws Exception {
        List<Map<String, Object>> selectedTuples = Select.execute(request, driver);
        driver.table.removeAll(selectedTuples);
        return selectedTuples;
    }

}
