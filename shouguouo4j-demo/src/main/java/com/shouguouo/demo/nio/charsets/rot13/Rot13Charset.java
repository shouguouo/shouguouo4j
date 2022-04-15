package com.shouguouo.demo.nio.charsets.rot13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * 新字符集 Rot13
 * 只针对英文字母进行额外的编码
 * (char) ((((c - 'a|A') + 13) % 26) + 'a|A')
 *
 * @author shouguouo
 * @date 2022-04-15 22:30:35
 */
public class Rot13Charset extends Charset {

    /**
     * the name of the base charset encoding we delegate to
     */
    private static final String BASE_CHARSET_NAME = "UTF-8";

    /**
     * Handle to the real charset we'll use for transcoding between
     * characters and bytes. Doing this allows us to apply the Rot13
     * algorithm to multibyte charset encodings. But only the
     * ASCII alpha chars will be rotated, regardless of the base encoding.
     */
    Charset baseCharset;

    /**
     * Initializes a new charset with the given canonical name and alias
     * set.
     *
     * @param canonicalName The canonical name of this charset
     * @param aliases An array of this charset's aliases, or null if it has no aliases
     * @throws IllegalCharsetNameException If the canonical name or any of the aliases are illegal
     */
    protected Rot13Charset(String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
        baseCharset = Charset.forName(BASE_CHARSET_NAME);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // Create a PrintStream that uses the Rot13 encoding
        PrintStream out = new PrintStream(System.out, false, "X-ROT13");
        String s;
        // Read all input and write it to the output.
        // As the data passes through the PrintStream,
        // it will be Rot13-encoded.
        while ((s = in.readLine()) != null) {
            out.println(s);
        }
        out.flush();
    }

    private void rot13(CharBuffer cb) {
        for (int pos = cb.position(); pos < cb.limit(); pos++) {
            char c = cb.get(pos);
            char a = '\u0000';
            // Is it lowercase alpha?
            if ((c >= 'a') && (c <= 'z')) {
                a = 'a';
            }
            // Is it uppercase alpha?
            if ((c >= 'A') && (c <= 'Z')) {
                a = 'A';
            }
            // If either, roll it by 13
            if (a != '\u0000') {
                c = (char) ((((c - a) + 13) % 26) + a);
                cb.put(pos, c);
            }
        }
    }

    @Override
    public boolean contains(Charset cs) {
        return false;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new Rot13Decoder(this, baseCharset.newDecoder());
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new Rot13Encoder(this, baseCharset.newEncoder());
    }

    private class Rot13Encoder extends CharsetEncoder {

        private final CharsetEncoder charsetEncoder;

        protected Rot13Encoder(Charset cs, CharsetEncoder baseEncoder) {
            super(cs, baseEncoder.averageBytesPerChar(), baseEncoder.maxBytesPerChar());
            this.charsetEncoder = baseEncoder;
        }

        @Override
        protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
            CharBuffer tempcb = CharBuffer.allocate(in.remaining());
            while (in.hasRemaining()) {
                tempcb.put(in.get());
            }
            tempcb.rewind();
            rot13(tempcb);
            charsetEncoder.reset();
            CoderResult encode = charsetEncoder.encode(tempcb, out, true);
            in.position(in.position() - tempcb.remaining());
            return encode;
        }
    }

    private class Rot13Decoder extends CharsetDecoder {

        private final CharsetDecoder charsetDecoder;

        protected Rot13Decoder(Charset cs, CharsetDecoder charsetDecoder) {
            super(cs, charsetDecoder.averageCharsPerByte(), charsetDecoder.maxCharsPerByte());
            this.charsetDecoder = charsetDecoder;
        }

        @Override
        protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
            charsetDecoder.reset();
            CoderResult decode = charsetDecoder.decode(in, out, true);
            rot13(out);
            return decode;
        }
    }
}

