package com.shouguouo.demo.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 更细粒度的控制
 * appendReplacement:将匹配的原值替换为replacement（包括匹配值开始索引到上个匹配值的结尾）追加到入参的StringBuffer上
 * appendTail:将剩余的字符添加到入参的StringBuffer上
 *
 * @author shouguouo
 * @date 2022-04-10 20:58:23
 */
public class RegexAppendTry {

    public static void main(String[] args) {
        String input = "Thanks, thanks very much";
        String regex = "([Tt])hanks";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        StringBuffer sb = new StringBuffer();
        // 循环直到遇到匹配
        while (matcher.find()) {
            // 使用group来判断
            if (matcher.group(1).equals("T")) {
                matcher.appendReplacement(sb, "Thank you");
            } else {
                matcher.appendReplacement(sb, "thank you");
            }
        }
        // 完成到StringBuffer的传输
        matcher.appendTail(sb);
        // 打印结果
        System.out.println(sb);
        // 在替换中使用$n转义
        sb.setLength(0);
        matcher.reset();
        String replacement = "$1hank you";
        //循环直到遇到匹配
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacement);
        }
        // 完成到StringBuffer传送
        matcher.appendTail(sb);
        // 打印结果
        System.out.println(sb);
        // 再来一次，简单的方法
        System.out.println(matcher.replaceAll(replacement));
        // 最后一次，只使用字符串
        System.out.println(input.replaceAll(regex, replacement));
    }
}
