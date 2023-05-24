package io.project.ResumeProcessorBot.utils;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtils {
    public static List<String> getPhrasesSentenceList(List<String> sentenceList) {
        String[] tempArray = new String[0];
        List<String> tempList = new ArrayList<>();
        for (String sentence : sentenceList) {
            if (sentence.contains(",")) {
                tempArray = sentence.split(",\\s*");
            }
            tempList.addAll(Arrays.asList(tempArray));
        }
        return tempList;
    }
    public static List<String> getSentenceList(String text) {
        List<String> sentencesList = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next())
        {
            String sentence = text.substring(start,end).trim();
            sentencesList.add(sentence);
        }
        return sentencesList;
    }

    public static List<List<String>> turnListIntoListOfLists(List<String> list) {
        List<List<String>> lists = new ArrayList<>();
        for (String str : list) {
            String[] tempArray = str.split("\\s|\\.\\s*|,\\s*");
            lists.add(Arrays.asList(tempArray));
        }
        return lists;
    }

    public static List<String> turnListOfListsIntoList(List<List<String>> lists) {
        List<String> list = new ArrayList<>();
        for (List<String> tempList : lists) {
            list.addAll(tempList);
        }
        return list;
    }



    public static List<String> getMatchList(List<List<String>> lists1, List<List<String>> lists2) {
        List<List<String>> matchList = new ArrayList<>();

        for (List<String> list1 : lists1) {
            for (List<String> list2 : lists2) {
                List<String> common = new ArrayList<>(list1);
                common.retainAll(list2);
                if (common.size() > 0) {
                    matchList.add(common);
                }
            }
        }

        List<String> match = new ArrayList<>();
        for (List<String> list : matchList) {
            match.addAll(list);
        }

        return match.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
