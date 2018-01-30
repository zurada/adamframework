package org.adam.framework.core;

class AdamRuntimeException extends RuntimeException {


    public static AdamRuntimeException noSuchPackage(String packageName){
        return new AdamRuntimeException(String.format("No such package - please make sure the package %s exists.", packageName));
    }

    public static AdamRuntimeException noSuchDirectory(String directory){
        return new AdamRuntimeException(String.format("No such directory - please make sure the directory %s exists.", directory));
    }

    public static AdamRuntimeException noSuchClass(String className){
        return new AdamRuntimeException(String.format("No such class - please make sure the class %s exists.", className));
    }

    public static AdamRuntimeException problemDuringBeansScan(String className){
        return new AdamRuntimeException(String.format("Problem with scanning AdamBeans and AdamInterfaces: %s.", className));
    }

    public static AdamRuntimeException problemDuringPrototypeCreation(String className){
        return new AdamRuntimeException(String.format("Problem creating prototype proxy for class: %s.", className));
    }
    public static AdamRuntimeException incorrectClassType(String className){
        return new AdamRuntimeException(String.format("Problem with getting instance of a class: %s. Please make sure it is an AdamInterface interface.", className));
    }

    private AdamRuntimeException(String s) {
        super(s);
    }
}
