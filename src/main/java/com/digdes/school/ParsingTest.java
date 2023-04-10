package com.digdes.school;

import com.digdes.school.Commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParsingTest {
    public List<Map<String, Object>> table = new ArrayList<>();
    public void parseRequest(String request) throws Exception {
        List<String> requestWords = Arrays.stream(request.split("[ ,]"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        String firstWord = requestWords.get(0);
        if(!firstWord.matches("(?i)(INSERT)|(UPDATE)|(DELETE)|(SELECT)")) {
            throw new Exception();
        }


        if(firstWord.matches("(?i)INSERT")) {
            System.out.println(Insert.execute(requestWords, this));
            System.out.println("INSERT");
        }

        if(firstWord.matches("(?i)UPDATE")) {
            System.out.println(Update.execute(requestWords, this));
            System.out.println("UPDATE");
        }

        if(firstWord.matches("(?i)DELETE")) {
            Delete.execute(requestWords, this);
            System.out.println("DELETE");
        }

        if(firstWord.matches("(?i)SELECT")) {
            System.out.println(Select.execute(requestWords, this));
            System.out.println("SELECT");
        }
    }
}
