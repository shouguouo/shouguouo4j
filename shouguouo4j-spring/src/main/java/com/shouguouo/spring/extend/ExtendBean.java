package com.shouguouo.spring.extend;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * BeanFactoryPostProcessor.postProcessBeanFactory -->
 * Aware -->
 * BeanPostProcessor.postProcessBeforeInitialization -->
 * initialBean -->
 * initMethod -->
 * BeanPostProcessor.postProcessAfterInitialization -->
 * DisposableBean -->
 * destroyMethod
 *
 * @author shouguouo
 * @date 2022-08-24 20:04:29
 */
@Component
public class ExtendBean implements InitializingBean, DisposableBean, BeanDefinitionRegistryPostProcessor, ImportBeanDefinitionRegistrar, BeanPostProcessor {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 0
        System.out.println("ImportBeanDefinitionRegistrar");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 1
        System.out.println("InitializingBean");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 2
        System.out.println("BeanDefinitionRegistryPostProcessor");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 3
        System.out.println("BeanFactoryPostProcessor");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 4
        System.out.println("BeanPostProcessor Before");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 5
        System.out.println("BeanPostProcessor after");
        return bean;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destory");
    }
}
