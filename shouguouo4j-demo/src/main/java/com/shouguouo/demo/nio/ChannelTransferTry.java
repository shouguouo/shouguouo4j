package com.shouguouo.demo.nio;

import com.google.common.base.Stopwatch;
import com.shouguouo.common.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

/**
 * 通道间传输 底层依赖操作系统的sendfile()系统调用
 * 对比通道传输和传统IO之间的性能差异：大量小文件性能差不多，大文件通道间传输有显著优势
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
        Stopwatch stopwatch = Stopwatch.createStarted();
        // 160ms 1.002 min 2.427 min
        // catFiles(System.out, pathList);
        // 200ms 56.70 s 47.02 s
        catFiles(Channels.newChannel(System.out), pathList.toArray(new String[0]));
        System.out.println(stopwatch.stop());

    }

    private static void catFiles(PrintStream target, List<String> pathList) throws Exception {
        for (String path : pathList) {
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                target.write(bytes, 0, len);
            }
            bufferedInputStream.close();
            fis.close();
        }
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
