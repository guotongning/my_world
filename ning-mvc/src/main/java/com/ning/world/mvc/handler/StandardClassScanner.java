package com.ning.world.mvc.handler;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描某个路径下的所有类文件
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public class StandardClassScanner implements ClassScanner {

    @Override
    public Set<Class<?>> scan(String basePackage) {
        Set<Class<?>> res = new HashSet<>();
        String path = basePackage.replace(".", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) {
            return res;
        }
        String protocol = url.getProtocol();
        if ("jar".equalsIgnoreCase(protocol)) {
            try {
                res.addAll(getJarClasses(url, basePackage));
            } catch (IOException e) {
                e.printStackTrace();
                return res;
            }
        } else if ("file".equalsIgnoreCase(protocol)) {
            res.addAll(getFileClasses(url, basePackage));
        }
        return res;
    }

    private Set<Class<?>> getFileClasses(URL url, String packagePath) {
        Set<Class<?>> res = new HashSet<>();
        String filePath = url.getFile();
        File dir = new File(filePath);
        String[] list = dir.list();
        if (list == null) return res;
        for (String classPath : list) {
            if (classPath.endsWith(".class")) {
                classPath = classPath.replace(".class", "");
                try {
                    Class<?> aClass = Class.forName(packagePath + "." + classPath);
                    res.add(aClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                res.addAll(scan(packagePath + "." + classPath));
            }
        }
        return res;
    }

    private Set<Class<?>> getJarClasses(URL url, String packagePath) throws IOException {
        Set<Class<?>> res = new HashSet<>();
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        if (conn != null) {
            JarFile jarFile = conn.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.contains(".class") && name.replaceAll("/", ".").startsWith(packagePath)) {
                    String className = name.substring(0, name.lastIndexOf(".")).replace("/", ".");
                    try {
                        Class<?> clazz = Class.forName(className);
                        res.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res;
    }

}
