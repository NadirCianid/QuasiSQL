package com.digdes.school;

import com.digdes.school.Commands.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class  Main {
    public static void main(String[] args) throws Exception {
        List<String> conditions = Arrays.stream("'active'=false, 'cost' = 10.1, 'age'=   53 ,  'active'=false".split("[ ]"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        System.out.println(conditions);
        System.out.println(Command.convertToUnaryWords(conditions));

        ParsingTest parser =  new ParsingTest();
        try {
          parser.parseRequest("   insert values 'active'=false, 'cost'=10.1");
          parser.parseRequest("INSERT VALUES 'lastName' = 'Федоров',  'id'=5, 'age'=40, 'active'=true");
          parser.parseRequest("Update values 'lastName' = 'Чепушила'");
          parser.parseRequest("select where 'id' = 3 and 'active' =false");
          parser.parseRequest("SelecT");
          parser.parseRequest("delete where 'id' = 3");
          parser.parseRequest("select");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}