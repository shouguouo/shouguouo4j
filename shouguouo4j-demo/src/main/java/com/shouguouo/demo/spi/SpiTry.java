package com.shouguouo.demo.spi;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.ServiceLoader;

/**
 * @author shouguouo
 * @date 2021-06-26 22:22:35
 */
public class SpiTry {

    public static void main(String[] args) {
        ServiceLoader<Hi> loadedHi = ServiceLoader.load(Hi.class);
        for (Hi impl : loadedHi) {
            impl.sayHi();
        }
        Enumeration<Driver> loadedDriver = DriverManager.getDrivers();
        while (loadedDriver.hasMoreElements()) {
            Driver driver = loadedDriver.nextElement();
            System.out.println(driver);
        }
    }

}
