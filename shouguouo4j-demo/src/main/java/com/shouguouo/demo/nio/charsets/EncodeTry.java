package com.shouguouo.demo.nio.charsets;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author shouguouo
 * @date 2022-04-15 16:21:51
 */
public class EncodeTry {

    public static void main(String[] args) {
        String input = "\u00bfMa\u00f1ana?";
        String[] charsetNames = {
                "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE",
                "UTF-16LE", "UTF-16"
        };
        for (String charsetName : charsetNames) {
            doEncode(Charset.forName(charsetName), input);
        }
        // 获取当前JVM支持的所有字符集
        SortedMap<String, Charset> charsetMap = Charset.availableCharsets();
        for (Map.Entry<String, Charset> entry : charsetMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().aliases() + ":" + entry.getValue().displayName());
        }
    }

    private static void doEncode(Charset cs, String input) {
        ByteBuffer bb = cs.encode(input);
        System.out.println("Charset: " + cs.name());
        System.out.println(" Input: " + input);
        System.out.println("Encoded: ");
        for (int i = 0; bb.hasRemaining(); i++) {
            int b = bb.get();
            int ival = ((int) b) & 0xff;
            char c = (char) ival;
            // Keep tabular alignment pretty
            if (i < 10) {
                System.out.print(" ");
            }
            // Print index number
            System.out.print(" " + i + ": ");
            // Better formatted output is coming someday...
            if (ival < 16) {
                System.out.print("0");
            }
            // Print the hex value of the byte
            System.out.print(Integer.toHexString(ival));
            // If the byte seems to be the value of a
            // printable character, print it. No guarantee
            // it will be.
            if (Character.isWhitespace(c) ||
                    Character.isISOControl(c)) {
                System.out.println("");
            } else {
                System.out.println(" (" + c + ")");
            }
        }
        System.out.println("");
    }
}
