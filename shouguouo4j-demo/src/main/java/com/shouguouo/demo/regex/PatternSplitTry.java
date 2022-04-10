package com.shouguouo.demo.regex;

import java.util.regex.Pattern;

/**
 * @author shouguouo
 * @date 2022-04-10 11:24:31
 */
public class PatternSplitTry {

    public static void main(String[] args) {
        String input = "poodle zoo";
        Pattern space = Pattern.compile(" ");
        Pattern d = Pattern.compile("d");
        Pattern o = Pattern.compile("o");
        Pattern[] patterns = { space, d, o };
        int[] limits = { 1, 2, 5, -2, 0 };
        generateTable(input, patterns, limits);
    }

    private static void generateTable(String input, Pattern[] patterns, int[] limits) {
        System.out.println("<?xml version='1.0'?>");
        System.out.println("<table>");
        System.out.println("\t<row>");
        System.out.println("\t\t<head>Input: "
                + input + "</head>");
        for (Pattern pattern : patterns) {
            System.out.println("\t\t<head>Regex: <value>"
                    + pattern.pattern() + "</value></head>");
        }
        System.out.println("\t</row>");
        for (int limit : limits) {
            System.out.println("\t<row>");
            System.out.println("\t\t<entry>Limit: "
                    + limit + "</entry>");
            for (Pattern pattern : patterns) {
                String[] tokens = pattern.split(input, limit);
                System.out.print("\t\t<entry>");
                for (String token : tokens) {
                    System.out.print("<value>"
                            + token + "</value>");
                }
                System.out.println("</entry>");
            }
            System.out.println("\t</row>");
        }
        System.out.println("</table>");
    }
}
