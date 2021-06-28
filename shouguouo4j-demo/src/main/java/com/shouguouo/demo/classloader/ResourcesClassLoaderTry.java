package com.shouguouo.demo.classloader;

/**
 * @author shouguouo
 * @date 2021-06-27 19:49:47
 */
public class ResourcesClassLoaderTry {

    public static void main(String[] args) throws ClassNotFoundException {
        ResourcesClassLoader resourcesClassLoader = new ResourcesClassLoader();
        Class<?> loaded = resourcesClassLoader.loadClass("com.shouguouo.spi.impl.ChinaHi");
        System.out.println(loaded);
    }
}
