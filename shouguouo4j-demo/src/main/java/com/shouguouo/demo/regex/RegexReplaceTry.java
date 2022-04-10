package com.shouguouo.demo.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则替换 $1代表被替换的第一个字符
 * ([bB])yte $1ite "Bytes is bytes"
 *
 * output:
 * regex: '([bB])yte'
 * replacement: '$1ite'
 * --------------------------
 * input: 'Bytes is bytes'
 * replaceFirst( ): 'Bites is bytes'
 * replaceAll( ): 'Bites is bites'
 *
 * @author shouguouo
 * @date 2022-04-10 20:48:37
 */
public class RegexReplaceTry {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: regex replacement input ...");
            return;
        }
        String regex = args[0];
        String replace = args[1];
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("");
        System.out.println(" regex: '" + regex + "'");
        System.out.println(" replacement: '" + replace + "'");
        for (int i = 2; i < args.length; i++) {
            System.out.println("--------------------------");
            // 重设
            matcher.reset(args[i]);
            System.out.println(" input: '" + args[i] + "'");
            System.out.println("replaceFirst( ): '" + matcher.replaceFirst(replace) + "'");
            System.out.println(" replaceAll( ): '" + matcher.replaceAll(replace) + "'");
        }
    }
}
