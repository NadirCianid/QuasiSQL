package com.digdes.school;

import java.util.List;
import java.util.Map;

public class  Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter =  new JavaSchoolStarter();
        try {
            //Вставка строки в коллекцию
            List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName'='оРоЛ', 'id' = 3" );
            //Изменение значения которое выше записывали
            List<Map<String,Object>> result2 = starter.execute("UPDATE VALUES  'lastName' = null , 'active'=false, 'cost'=10.1 where 'lastName'ilike'оРоЛ' And 'id'=3 ");
            //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
            List<Map<String,Object>> result3 = starter.execute("SELECT where 'lastName'ilike'о.+ол' and 'lastName' = 'оРоЛ'");
            List<Map<String,Object>> result4 = starter.execute("delete WHERE 'id'=5 ");
            List<Map<String,Object>> result5 = starter.execute("SELECT");

            System.out.println(result1);
            System.out.println(result2);
            System.out.println(result3);
            System.out.println(result4);
            System.out.println(result5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}