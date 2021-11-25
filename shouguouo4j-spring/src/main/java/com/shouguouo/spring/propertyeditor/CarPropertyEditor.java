package com.shouguouo.spring.propertyeditor;

import java.beans.PropertyEditorSupport;

/**
 * @author shouguouo
 * @date 2021-11-25 14:47:44
 */
public class CarPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            return;
        }
        String[] infos = text.split(",");
        Car car = new Car();
        car.setBrand(infos[0]);
        car.setMaxSpeed(Integer.parseInt(infos[1]));
        car.setPrice(Double.parseDouble(infos[2]));
        //2. 调用父类的setValue()方法设置转换后的属性对象
        setValue(car);
    }
}
