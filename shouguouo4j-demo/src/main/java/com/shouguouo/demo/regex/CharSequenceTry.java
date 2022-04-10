package com.shouguouo.demo.regex;

import java.nio.CharBuffer;

/**
 * String\StringBuffer\CharBuffer 继承自CharSequence
 *
 * String 不可变，输出内容即为String本身
 * StringBuffer 输出反映当时StringBuffer的状态
 * CharBuffer 输出自position到limit之间的字符
 *
 * output:
 * length=11, content='Hello World'
 * length=11, content='Hello World'
 * length=23, content='Goodbye my almost lover'
 * length=11, content='Hello World'
 * length=11, content='Seeya World'
 * length=20, content='Seeya Worldxxxxxxxxx'
 *
 * @author shouguouo
 * @date 2022-04-10 11:12:22
 */
public class CharSequenceTry {

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("Hello World");
        CharBuffer cb = CharBuffer.allocate(20);
        CharSequence cs = "Hello World";
        // 来源String
        printCharSequence(cs);
        cs = sb;
        // 来源StringBuffer
        printCharSequence(cs);
        // 更改StringBuffer
        sb.setLength(0);
        sb.append("Goodbye my almost lover");
        // 不变的cs产生了不一样的结果
        printCharSequence(sb);
        // CharBuffer
        cs = cb;
        cb.put("xxxxxxxxxxxxxxxxxxxx");
        cb.clear();
        cb.put("Hello World");
        cb.flip();
        // length=11, content='Hello World'
        printCharSequence(cs);
        cb.mark();
        cb.put("Seeya");
        cb.reset();
        // length=11, content='Seeya World'
        printCharSequence(cs);
        cb.clear();
        // length=20, content='Seeya Worldxxxxxxxxx'
        printCharSequence(cs);
    }

    private static void printCharSequence(CharSequence cs) {
        System.out.println("length=" + cs.length() + ", content='" + cs + "'");
    }
}
