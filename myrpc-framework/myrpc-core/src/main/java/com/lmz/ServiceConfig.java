package com.lmz;

/**
 * @author 老疯狗
 * @Title ServiceConfig
 * @date 2024/7/14
 */
public class ServiceConfig<T> {
    private Class<T> interfaceProvider;
    private Object ref;

    public Class<T> getInterfaceProvider() {
        return interfaceProvider;
    }

    public void setInterfaceProvider(Class<T> interfaceProvider) {
        this.interfaceProvider = interfaceProvider;
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }
}
