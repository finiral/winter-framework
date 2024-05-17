package mg.itu.prom16.utils;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ControllerUtils {
    boolean isController(Class<?> c) {
        return c.isAnnotationPresent(Controller.class);
    }

    public List<Class<?>> getAllClassAnnotation(String packageName,Class annotation) throws Exception {
        List<Class<?>> res=new ArrayList<Class<?>>();
        //répertoire racine du package
        String path = this.getClass().getClassLoader().getResource(packageName.replace('.', '/')).getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        File packageDir = new File(decodedPath);

        // parcourir tous les fichiers dans le répertoire du package
        File[] files = packageDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> classe = Class.forName(className);
                    if (classe.isAnnotationPresent(annotation)) {
                        res.add(classe);
                    }
                }
            }
        }
        return res;

    }
}
