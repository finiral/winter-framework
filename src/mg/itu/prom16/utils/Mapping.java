package mg.itu.prom16.utils;
import java.util.Objects;
import java.util.Set;

import mg.itu.prom16.object.VerbMethod;

public class Mapping {
    String className;
    Set<VerbMethod> verbmethods;
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public Set<VerbMethod> getVerbmethods() {
        return verbmethods;
    }
    public void setVerbmethods(Set<VerbMethod> verbmethods) {
        this.verbmethods = verbmethods;
    }
    public Mapping() {
    }
    public Mapping(String className, Set<VerbMethod> verbmethods) {
        setClassName(className);
        setVerbmethods(verbmethods);
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mapping mapping = (Mapping) o;
        return Objects.equals(className, mapping.className) && 
               Objects.equals(verbmethods, mapping.verbmethods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, verbmethods);
    }
}
