package com.shouguouo.demo.regex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配文件内容
 * Usage: regex file [...]
 *
 * @author shouguouo
 * @date 2022-04-10 11:37:15
 */
public class RegexGrepTry {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: regex file [...]");
            return;
        }
        Pattern pattern = Pattern.compile(args[0]);
        Matcher matcher = pattern.matcher("");
        for (int i = 1; i < args.length; i++) {
            String file = args[i];
            BufferedReader br;
            String line;
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                System.err.println("Cannot read '" + file + "': " + e.getMessage());
                continue;
            }
            while ((line = br.readLine()) != null) {
                // 重置matcher
                matcher.reset(line);
                if (matcher.find()) {
                    System.out.println(file + ": " + line);
                }
            }
            br.close();
        }
    }
}
