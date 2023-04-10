package com.digdes.school.Commands;

import com.digdes.school.ParsingTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Delete extends Command{
    public static List<Map<String, Object>> execute(List<String> request, ParsingTest driver) throws Exception {
        driver.table.removeAll(Select.execute(request, driver));
        return new ArrayList<>();
    }

}
