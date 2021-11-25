package com.shouguouo.spring.propertyeditor;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-25 15:32:52
 */
@Component
public class CarPropertyEditorRegister extends CustomEditorConfigurer {

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);
        beanFactory.registerCustomEditor(Car.class, CarPropertyEditor.class);
    }
}
