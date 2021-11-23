package com.shouguouo.demo.io;

import java.io.File;
import java.io.IOException;

/**
 * @author shouguouo
 * @date 2021-11-23 10:02:07
 */
public class FilePathTry {

    public static void main(String[] args) throws IOException {
        File file = new File("../io/a.txt");
        String path = file.getPath();
        String absolutePath = file.getAbsolutePath();
        String canonicalPath = file.getCanonicalPath();
        // relative path
        System.out.println(path);
        // absolute path
        System.out.println(absolutePath);
        // unify
        System.out.println(canonicalPath);
    }

}
