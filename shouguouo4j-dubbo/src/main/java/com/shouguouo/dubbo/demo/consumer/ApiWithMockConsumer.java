package com.shouguouo.dubbo.demo.consumer;

import com.shouguouo.common.util.OutputUtils;
import com.shouguouo.dubbo.demo.sdk.GreetingService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author shouguouo
 * @date 2022-06-05 12:22:34
 */
public class ApiWithMockConsumer {

    public static void main(String[] args) {
        ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-consumer"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(GreetingService.class);
        referenceConfig.setTimeout(3000);

        referenceConfig.setGroup("shouguouo");
        referenceConfig.setVersion("1.0.0");
        // 不校验
        referenceConfig.setCheck(false);
        // API支持 force/fail// return.... 默认会取相同包路径的Mock Class
        // @see org.apache.dubbo.config.utils.ConfigValidationUtils.checkMock
        // referenceConfig.setMock("force");
        // mock
        // @see MockClusterInvoker
        ExtensionLoader<RegistryFactory> registryFactoryLoader = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
        RegistryFactory registryFactory = registryFactoryLoader.getAdaptiveExtension();
        Registry registry = registryFactory.getRegistry(URL.valueOf("zookeeper://127.0.0.1:2181"));
        registry.register(URL.valueOf("override://0.0.0.0/com.shouguouo.dubbo.demo.sdk.GreetingService?category=configurators"
                + "&dynamic=false&application=shouguouo-dubbo-consumer&mock=" + "force" + ":return+sb&group=shouguouo&version=1.0.0"));

        GreetingService greetingService = referenceConfig.get();

        RpcContext.getClientAttachment().setAttachment("company", "shouguouo");

        OutputUtils.printlnWithCurrentThread(greetingService.sayHello("world"));

    }
}
