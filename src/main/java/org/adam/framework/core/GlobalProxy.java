package org.adam.framework.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.adam.framework.aspect.transaction.TransactionAspect;

class GlobalProxy implements InvocationHandler {

    private Object object;

    GlobalProxy(Class clazzImpl) throws IllegalAccessException, InstantiationException, NoSuchMethodException,
                                        InvocationTargetException {
        this.object = clazzImpl.getConstructor().newInstance();
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Method objectMethod = this.object.getClass().getMethod(method.getName(), method.getParameterTypes());
        TransactionAspect.pointcutFor(objectMethod);
        return objectMethod.invoke(object, objects);
    }


}
