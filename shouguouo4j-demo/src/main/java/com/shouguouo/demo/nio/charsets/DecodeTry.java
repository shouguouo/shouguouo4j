package com.shouguouo.demo.nio.charsets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 通过控制CoderResult来读取输入到输出，需经过指定编码的编码
 *
 * @author shouguouo
 * @date 2022-04-15 17:21:40
 */
public class DecodeTry {

    /**
     * Test charset decoding in the general case, detecting and handling
     * buffer under/overflow and flushing the decoder state at end of
     * input.
     * This code reads from stdin and decodes the ASCII-encoded byte
     * stream to chars. The decoded chars are written to stdout. This
     * is effectively a 'cat' for input ascii files, but another charset
     * encoding could be used by simply specifying it on the command line.
     */
    public static void main(String[] args) throws IOException {
        String charsetName = "utf8";
        decodeChannel(Channels.newChannel(System.in),
                new OutputStreamWriter(System.out),
                Charset.forName(charsetName));
    }

    public static void decodeChannel(ReadableByteChannel source,
            Writer writer, Charset charset)
            throws UnsupportedCharsetException, IOException {
        // Get a decoder instance from the Charset
        CharsetDecoder decoder = charset.newDecoder();
        // Tell decoder to replace bad chars with default mark
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        // Allocate radically different input and output buffer sizes
        // for testing purposes
        ByteBuffer bb = ByteBuffer.allocateDirect(16 * 1024);
        CharBuffer cb = CharBuffer.allocate(57);
        // Buffer starts empty; indicate input is needed
        CoderResult result = CoderResult.UNDERFLOW;
        boolean eof = false;
        while (!eof) {
            // Input buffer underflow; decoder wants more input
            if (result == CoderResult.UNDERFLOW) {
                // decoder consumed all input, prepare to refill
                bb.clear();
                // Fill the input buffer; watch for EOF
                eof = (source.read(bb) == -1);
                // Prepare the buffer for reading by decoder
                bb.flip();
            }
            // Decode input bytes to output chars; pass EOF flag
            result = decoder.decode(bb, cb, eof);
            // If output buffer is full, drain output
            if (result == CoderResult.OVERFLOW) {
                drainCharBuf(cb, writer);
            }
        }
        // Flush any remaining state from the decoder, being careful
        // to detect output buffer overflow(s)
        while (decoder.flush(cb) == CoderResult.OVERFLOW) {
            drainCharBuf(cb, writer);
        }
        // Drain any chars remaining in the output buffer
        drainCharBuf(cb, writer);
        // Close the channel; push out any buffered data to stdout
        source.close();
        writer.flush();
    }

    /**
     * Helper method to drain the char buffer and write its content to
     * the given Writer object. Upon return, the buffer is empty and
     * ready to be refilled.
     *
     * @param cb A CharBuffer containing chars to be written.
     * @param writer A Writer object to consume the chars in cb.
     */
    static void drainCharBuf(CharBuffer cb, Writer writer)
            throws IOException {
        cb.flip(); // Prepare buffer for draining
        // This writes the chars contained in the CharBuffer but
        // doesn't actually modify the state of the buffer.
        // If the char buffer was being drained by calls to get( ),
        // a loop might be needed here.
        if (cb.hasRemaining()) {
            writer.write(cb.toString());
        }
        cb.clear(); // Prepare buffer to be filled again
        // 刷新输出流
        // writer.flush();
    }
}
