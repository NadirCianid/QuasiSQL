package com.digdes.school;

import com.digdes.school.Commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParsingTest {
    public List<Map<String, Object>> table = new ArrayList<>();
    public List<Map<String, Object>> parseRequest(String request) throws Exception {
        List<String> requestWords = Arrays.stream(request.split("[ ]"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        String firstWord = requestWords.get(0);
        if(!firstWord.matches("(?i)(INSERT)|(UPDATE)|(DELETE)|(SELECT)")) {
            throw new Exception();
        }


        if(firstWord.matches("(?i)INSERT")) {
            return Insert.execute(requestWords, this);
        }

        if(firstWord.matches("(?i)UPDATE")) {
            return Update.execute(requestWords, this);
        }

        if(firstWord.matches("(?i)DELETE")) {
            return Delete.execute(requestWords, this);
        }

        if(firstWord.matches("(?i)SELECT")) {
            return Select.execute(requestWords, this);
        }
        return null;
    }
}
