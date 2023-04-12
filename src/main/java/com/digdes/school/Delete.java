package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Delete {
    public static List<Map<String, Object>> execute(List<String> request, JavaSchoolStarter driver) throws Exception {
        List<Map<String, Object>> selectedTuples = Select.execute(request, driver);
        driver.table.removeAll(selectedTuples);
        return selectedTuples;
    }

}
