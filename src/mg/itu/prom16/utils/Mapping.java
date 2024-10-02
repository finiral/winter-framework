package mg.itu.prom16.utils;

import java.lang.reflect.Method;

public class Mapping {
    String className;
    Method method;
    String verb;
    public String getVerb() {
        return verb;
    }
    public void setVerb(String verb) {
        this.verb = verb;
    }
    public Method getMethodName() {
        return method;
    }
    public void setMethod(Method method) {
        this.method = method;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public Mapping() {
    }
    public Mapping(String className, Method method,String verb) {
        setClassName(className);
        setMethod(method);
        setVerb(verb);
    }
    
}
