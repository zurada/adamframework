package org.adam.framework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.adam.framework.annotations.AdamBean;
import org.adam.framework.annotations.AdamInterface;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AdamContainer {

    private static AdamContainer container = new AdamContainer();
    private final Map<String, Object> singletonProxies = new HashMap<>();
    private final Map<String, Class> prototypeProxiesDefs = new HashMap<>();
    private String basePackage;

    private AdamContainer() {
        log.info("Adam Framework v0.1");
        log.info("Creating Adam Container");
    }

    public static AdamContainer getInstance() {
        return container;
    }

    public void init(String aPackage) {
        singletonProxies.clear();
        prototypeProxiesDefs.clear();
        log.info("AdamCointainer initialization.");
        basePackage = aPackage;
        scanAdamBeans();
    }

    public Object createPrototype(Class aInterface) {
        if(aInterface.isInterface()) {
            try {
                return generateProxy(aInterface);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw AdamRuntimeException.problemDuringPrototypeCreation(aInterface.getName());
            }
        } else {
            throw AdamRuntimeException.incorrectClassType(aInterface.getName());
        }

    }

    public Object getSingleton(Class aInterface) {
        if(aInterface.isInterface()) {
            return singletonProxies.get(aInterface.getCanonicalName());
        } else {
            throw AdamRuntimeException.incorrectClassType(aInterface.getName());
        }
    }

    private void scanAdamBeans() {
        log.info("Scanning basePackage:" + basePackage);
        generateAdamBeans();
        log.info("Scan completed. Created proxies size: " + singletonProxies.keySet().size());
    }

    private void generateAdamBeans() {
        PackageScanner.scanBasePackage(basePackage).forEach(aClass -> {
            if(aClass.getAnnotation(AdamBean.class) != null) {
                if(aClass.isInterface()) {
                    throw AdamRuntimeException.problemDuringBeansScan(aClass.getName());
                }
                generateAdamInterfaces(aClass);
            }
        });
    }

    private void generateAdamInterfaces(Class aClass) {
        Arrays.stream(aClass.getInterfaces()).forEach(aInterface -> {
            if(aInterface.getAnnotation(AdamInterface.class) != null) {
                putPrototypeDefinition(aClass, aInterface);
                try {
                    putSingletonProxy(aClass, aInterface);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw AdamRuntimeException.problemDuringBeansScan(aClass.getName());
                }
            }
        });
    }

    private Class putPrototypeDefinition(Class aClass, Class aInterface) {
        return prototypeProxiesDefs.put(aInterface.getCanonicalName(), aClass);
    }

    private Object putSingletonProxy(Class aClass, Class aInterface) throws IllegalAccessException,
                                                                            InstantiationException,
                                                                            NoSuchMethodException,
                                                                            InvocationTargetException {
        return singletonProxies.put(aInterface.getCanonicalName(), generateProxy(aClass, aInterface));
    }

    private Object generateProxy(Class aInterface) throws IllegalAccessException, InstantiationException,
                                                          NoSuchMethodException, InvocationTargetException {
        return generateProxy(prototypeProxiesDefs.get(aInterface.getCanonicalName()), aInterface);
    }

    private Object generateProxy(Class aClass, Class aInterface) throws IllegalAccessException, InstantiationException,
                                                                        NoSuchMethodException,
                                                                        InvocationTargetException {
        return Proxy.newProxyInstance(aInterface.getClassLoader(),
                new Class[] { aInterface }, new GlobalProxy(aClass));
    }

}
