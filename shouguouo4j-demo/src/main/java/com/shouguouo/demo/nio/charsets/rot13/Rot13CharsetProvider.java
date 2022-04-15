package com.shouguouo.demo.nio.charsets.rot13;

import com.google.common.collect.Sets;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Iterator;

/**
 * 字符集SPI实现
 *
 * @author shouguouo
 * @date 2022-04-15 22:46:22
 */
public class Rot13CharsetProvider extends CharsetProvider {

    private static final String CHARSET_NAME = "X-ROT13";

    private final Charset rot13;

    public Rot13CharsetProvider() {
        this.rot13 = new Rot13Charset(CHARSET_NAME, new String[0]);
    }

    @Override
    public Iterator<Charset> charsets() {
        return Sets.newHashSet(rot13).iterator();
    }

    @Override
    public Charset charsetForName(String charsetName) {
        if (charsetName.equalsIgnoreCase(CHARSET_NAME)) {
            return rot13;
        }
        return null;
    }
}
