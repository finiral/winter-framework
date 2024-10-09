package mg.itu.prom16.object;

import java.lang.reflect.Method;
import java.util.Objects;

import mg.itu.prom16.utils.Mapping;

public class VerbMethod {
    String verb;
    Method methode;
    public String getVerb() {
        return verb;
    }
    public void setVerb(String verb) {
        this.verb = verb;
    }
    public Method getMethode() {
        return methode;
    }
    public void setMethode(Method methode) {
        this.methode = methode;
    }
    public VerbMethod() {
    }
    public VerbMethod(String verb, Method methode) {
        setVerb(verb);
        setMethode(methode);
    }

    @Override
    public boolean equals(Object o) {
        if(this.getClass().equals(o.getClass())){
            VerbMethod v=(VerbMethod) o;
            if(this.getVerb().equals(v.getVerb())){
                return true;
            }
        }
        return false;
    }

    /* @Override
    public int hashCode() {
        return Objects.hash(className, verbmethods);
    } */

}
