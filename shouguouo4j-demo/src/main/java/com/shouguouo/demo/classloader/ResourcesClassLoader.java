package com.shouguouo.demo.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author shouguouo
 * @date 2021-06-27 19:38:28
 */
public class ResourcesClassLoader extends ClassLoader {

    private static final String PATH = "class/";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        URL path = this.getClass().getClassLoader().getResource(PATH + name.replaceAll("\\.", "\\/") + ".class");
        if (path == null) {
            throw new ClassNotFoundException("无法加载指定类");
        }
        File file = new File(path.getPath());
        try (FileInputStream is = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int len;
            while ((len = is.read()) != -1) {
                bos.write(len);
            }
            byte[] data = bos.toByteArray();
            return defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }
}
