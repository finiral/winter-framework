package mg.itu.prom16.object;

import java.util.HashMap;

public class ModelView {
    String url; /* url de destination */
    String errorUrl; /* url de destination en cas d'erreur */
    String errorMethod; /* methode de destination en cas d'erreur */
    HashMap<String,Object> data; /* data à envoyer vers la vue */
    public String getUrl() {
        return url;
    }
    public HashMap<String, Object> getData() {
        return data;
    }
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getErrorUrl() {
        return errorUrl;
    }
    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }
    public ModelView(String url) {
        this.setUrl(url);
        this.data=new HashMap<String,Object>();
    }
    public ModelView() {
        this.data=new HashMap<String,Object>();
    }
    
    public void addObject(String variableName,Object o){
        this.data.put(variableName, o);
    }
    public String getErrorMethod() {
        if(this.errorMethod==null){
            return "GET";
        }
        return errorMethod;
    }
    public void setErrorMethod(String errorMethod) {
        this.errorMethod = errorMethod;
    }
    

}
