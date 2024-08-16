package com.lmz;

import com.lmz.discovery.Registry;
import com.lmz.discovery.RegistryConfig;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * @author 老疯狗
 * @Title ReferenceConfig
 * @date 2024/7/14
 */
@Slf4j
public class ReferenceConfig<T> {
    private Class<T> interfaceRef;

    private Registry registry;

    public Class<T> getInterface() {
        return interfaceRef;
    }

    public void setInterface(Class<T> interfaceRef) {
        this.interfaceRef = interfaceRef;
    }

    public T get() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class[] classes = {interfaceRef};
        Object helloProxy = Proxy.newProxyInstance(classLoader, classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


                InetSocketAddress address = registry.lookup(interfaceRef.getName());

                if (log.isDebugEnabled()) {
                    log.debug("服务调用方发现了服务【{}】的可用主机的【{}】", interfaceRef.getName(), address.getAddress() + ":" + address.getPort());
                }

                System.out.println("hello proxy");
                return null;
            }
        });
        return (T) helloProxy;
    }


    public Class<T> getInterfaceRef() {
        return interfaceRef;
    }

    public void setInterfaceRef(Class<T> interfaceRef) {
        this.interfaceRef = interfaceRef;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
