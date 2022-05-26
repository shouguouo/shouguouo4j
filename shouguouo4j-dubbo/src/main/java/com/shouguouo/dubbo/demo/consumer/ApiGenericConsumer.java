package com.shouguouo.dubbo.demo.consumer;

import com.alibaba.fastjson.JSON;
import com.shouguouo.common.util.OutputUtils;
import org.apache.dubbo.common.beanutil.JavaBeanDescriptor;
import org.apache.dubbo.common.beanutil.JavaBeanSerializeUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.io.UnsafeByteArrayInputStream;
import org.apache.dubbo.common.io.UnsafeByteArrayOutputStream;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shouguouo
 * @date 2022-05-26 19:46:05
 */
public class ApiGenericConsumer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 默认泛化调用的方式，根据类型
        OutputUtils.cuttingLine("defaultGenericInvoke");
        defaultGenericInvoke();
        // 可实现自定义类的序列化方式
        OutputUtils.cuttingLine("beanGenericInvoke");
        beanGenericInvoke();
        // 流 需开启CommonConstants.ENABLE_NATIVE_JAVA_GENERIC_SERIALIZE
        OutputUtils.cuttingLine("nativeGenericInvoke");
        nativeGenericInvoke();
    }

    private static void defaultGenericInvoke() {
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-consumer"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface("com.shouguouo.dubbo.demo.sdk.GreetingService");
        referenceConfig.setGeneric(CommonConstants.GENERIC_SERIALIZATION_DEFAULT);

        referenceConfig.setGroup("shouguouo");
        referenceConfig.setVersion("1.0.0");

        GenericService genericService = referenceConfig.get();

        RpcContext.getClientAttachment().setAttachment("company", "shouguouo");

        Object result = genericService.$invoke("sayHello",
                new String[] { "java.lang.String" },
                new Object[] { "world" });
        OutputUtils.printlnWithCurrentThread(JSON.toJSONString(result));

        Map<String, Object> map = new HashMap<>(3);
        map.put("class", "com.shouguouo.dubbo.demo.sdk.PoJo");
        map.put("id", "1996");
        map.put("name", "shouguouo");
        result = genericService.$invoke("testGeneric",
                new String[] { "com.shouguouo.dubbo.demo.sdk.PoJo" },
                new Object[] { map });
        OutputUtils.printlnWithCurrentThread(result.toString());
    }

    private static void beanGenericInvoke() {
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-consumer"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface("com.shouguouo.dubbo.demo.sdk.GreetingService");
        referenceConfig.setGeneric(CommonConstants.GENERIC_SERIALIZATION_BEAN);

        referenceConfig.setGroup("shouguouo");
        referenceConfig.setVersion("1.0.0");

        GenericService genericService = referenceConfig.get();

        RpcContext.getClientAttachment().setAttachment("company", "shouguouo");

        JavaBeanDescriptor param = JavaBeanSerializeUtil.serialize("world");
        Object result = genericService.$invoke("sayHello",
                new String[] { "java.lang.String" },
                new Object[] { param });
        OutputUtils.printlnWithCurrentThread(JavaBeanSerializeUtil.deserialize((JavaBeanDescriptor) result).toString());
    }

    private static void nativeGenericInvoke() throws IOException, ClassNotFoundException {
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-consumer"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface("com.shouguouo.dubbo.demo.sdk.GreetingService");
        referenceConfig.setGeneric(CommonConstants.GENERIC_SERIALIZATION_NATIVE_JAVA);

        referenceConfig.setGroup("shouguouo");
        referenceConfig.setVersion("1.0.0");

        GenericService genericService = referenceConfig.get();

        RpcContext.getClientAttachment().setAttachment("company", "shouguouo");

        UnsafeByteArrayOutputStream outputStream = new UnsafeByteArrayOutputStream();

        ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(CommonConstants.GENERIC_SERIALIZATION_NATIVE_JAVA)
                .serialize(null, outputStream).writeObject("world");
        Object result = genericService.$invoke("sayHello",
                new String[] { "java.lang.String" },
                new Object[] { outputStream.toByteArray() });
        UnsafeByteArrayInputStream inputStream = new UnsafeByteArrayInputStream((byte[]) result);
        OutputUtils.printlnWithCurrentThread(ExtensionLoader.getExtensionLoader(Serialization.class)
                .getExtension(CommonConstants.GENERIC_SERIALIZATION_NATIVE_JAVA)
                .deserialize(null, inputStream).readObject(String.class));
    }
}
