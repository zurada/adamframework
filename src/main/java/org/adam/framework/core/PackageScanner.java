package org.adam.framework.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

class PackageScanner {

    static List<Class> scanBasePackage(String packageName) {
        List<Class> classes = new ArrayList<>();

        Enumeration resources = getResources(packageName);
        getFiles(resources).forEach(dir -> classes.addAll(findClasses(dir, packageName)));
        return classes;
    }

    private static Enumeration<URL> getResources(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        try {
            return classLoader.getResources(path);
        } catch (IOException e) {
            throw AdamRuntimeException.noSuchPackage(packageName);
        }
    }

    private static List<File> getFiles(Enumeration resources) {
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        return dirs;
    }

    private static Collection<? extends Class> findClasses(File dir, String packageName) {
        final List<Class> classes = new ArrayList<>();
        if(!dir.exists()) {
            throw AdamRuntimeException.noSuchDirectory(dir.getPath());
        }
        listFilesInDirectory(dir, packageName, classes);
        return classes;
    }

    private static void listFilesInDirectory(File dir, String packageName, List<Class> classes) {
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(file -> {
            if(file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if(file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    throw AdamRuntimeException.noSuchClass(className);
                }
            }
        });
    }


}
