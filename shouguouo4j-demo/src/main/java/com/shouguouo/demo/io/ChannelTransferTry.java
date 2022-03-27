package com.shouguouo.demo.io;

import com.shouguouo.common.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

/**
 * TODO 对比通道传输和传统IO之间的性能差异
 *
 * @author shouguouo
 * @date 2022-03-27 21:40:17
 */
public class ChannelTransferTry {

    public static void main(String[] argv) throws Exception {
        //        if (argv.length == 0) {
        //            System.err.println("Usage: filename ...");
        //            return;
        //        }
        List<String> pathList = IOUtils.gatherFileAbsolutePath(new File("."));
        catFiles(Channels.newChannel(System.out), pathList.toArray(new String[0]));
    }

    // Concatenate the content of each of the named files to
    // the given channel. A very dumb version of 'cat'.
    private static void catFiles(WritableByteChannel target,
            String[] files)
            throws Exception {
        for (int i = 0; i < files.length; i++) {
            FileInputStream fis = new FileInputStream(files[i]);
            FileChannel channel = fis.getChannel();
            channel.transferTo(0, channel.size(), target);
            channel.close();
            fis.close();
        }
    }
}
