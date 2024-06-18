package mg.itu.prom16.utils;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.prom16.annotations.Controller;
import mg.itu.prom16.annotations.GetMapping;
import mg.itu.prom16.annotations.Param;
import mg.itu.prom16.object.ModelView;

public class Utils {
    boolean isController(Class<?> c) {
        return c.isAnnotationPresent(Controller.class);
    }

    public  Object parse(Object o,Class<?> typage) {
        if (typage.equals(int.class)) {
            return Integer.parseInt((String) o);
        } else if (typage.equals(double.class)) {
            return Double.parseDouble((String) o);
        } else if (typage.equals(boolean.class)) {
            return Boolean.parseBoolean((String) o);

        } else if (typage.equals(byte.class)) {
            return Byte.parseByte((String) o);

        } else if (typage.equals(float.class)) {
            return Float.parseFloat((String) o);

        } else if (typage.equals(short.class)) {
            return Short.parseShort((String) o);

        } else if (typage.equals(long.class)) {
            return Long.parseLong((String) o);

        }
        return typage.cast(o);
    }

    public List<String> getAllClassesStringAnnotation(String packageName, Class annotation) throws Exception {
        List<String> res = new ArrayList<String>();
        // répertoire racine du package
        if (this.getClass().getClassLoader().getResource(packageName.replace('.', '/')) == null) {
            throw new Exception("Le package " + packageName + " n'existe pas");
        }
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
                        res.add(classe.getName());
                    }
                }
            }
        }
        return res;

    }

    public HashMap<String, Mapping> scanControllersMethods(List<String> controllers) throws Exception {
        HashMap<String, Mapping> res = new HashMap<>();
        for (String c : controllers) {
            Class classe = Class.forName(c);
            /* Prendre toutes les méthodes de cette classe */
            Method[] meths = classe.getDeclaredMethods();
            for (Method method : meths) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String url = method.getAnnotation(GetMapping.class).url();
                    if (res.containsKey(url)) {
                        String existant = res.get(url).className + ":" + res.get(url).method.getName();
                        String nouveau = classe.getName() + ":" + method.getName();
                        throw new Exception("L'url " + url + " est déja mappé sur " + existant
                                + " et ne peut plus l'être sur " + nouveau);
                    }
                    /* Prendre l'annotation */
                    if(url.contains("?")){
                        url=url.split("?")[0];
                    }
                    res.put(url, new Mapping(c, method));
                }
            }
        }
        return res;
    }

    public String getURIWithoutContextPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    public Object[] getArgs(Map<String, String[]> params, Method method) throws Exception {
        List<Object> ls = new ArrayList<Object>();
        for (Parameter param : method.getParameters()) {
            String key = null;
            System.out.println(param.getName());
            if (params.containsKey(param.getName())) {
                key = param.getName();
            } else if (param.isAnnotationPresent(Param.class)
                    && params.containsKey(param.getAnnotation(Param.class).paramName())) {
                key = param.getAnnotation(Param.class).paramName();
            }
            /// Traitement type
            Class<?> typage = param.getType();
            /// Traitement values
            if (params.get(key).length == 1) {
                ls.add(this.parse(params.get(key)[0],typage));
            } else if (params.get(key).length > 1) {
                ls.add(this.parse(params.get(key),typage));
            } else if (params.get(key) == null) {
                ls.add(null);
            }
        }
        return ls.toArray();
    }

    public Method searchMethod(HashMap<String, Mapping> map, String path)
            throws Exception {
        if (map.containsKey(path)) {
            Method method = map.get(path).method;
            return method;
        } else {
            throw new Exception("Aucune méthode associé a cette url");
        }
    }

    public Object searchExecute(HashMap<String, Mapping> map, String path, Map<String, String[]> params)
            throws Exception {
        Method methode = this.searchMethod(map, path);
        Mapping m = map.get(path);
        Class<?> classe = Class.forName(m.getClassName());
        Object appelant = classe.getDeclaredConstructor().newInstance((Object[]) null);
        Object res = methode.invoke(appelant,this.getArgs(params, methode));
        if(!(res instanceof String) && !(res instanceof ModelView)){
            throw new Exception("La méthode " + methode.getName() + " ne retourne ni String ni ModelView");
        }
        return res;
    }
}
