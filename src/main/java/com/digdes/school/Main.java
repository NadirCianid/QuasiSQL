package com.digdes.school;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class  Main {
    public static void main(String[] args) throws Exception {

        ParsingTest parser =  new ParsingTest();
        try {
          parser.parseRequest("   insert values 'active'=false, 'cost'=10.1");
          parser.parseRequest("INSERT VALUES 'lastName' = 'Федоров',  'id'=5, 'age'=40, 'active'=true");
          System.out.println(parser.table);
          //  System.out.println(parser.parseRequest("Update values 'lastName' = 'Чепушила'  where 'id' = 5 and 'active' =true"));
          System.out.println(parser.parseRequest("select where 'id'  = 5  or  'active' =false and 'age'=30 "));
          parser.parseRequest("SelecT");
          parser.parseRequest("delete where 'id' = 3 or 'age' = 40");
          parser.parseRequest("select");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}