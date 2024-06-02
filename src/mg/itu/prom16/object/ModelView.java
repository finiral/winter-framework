package mg.itu.prom16.object;

import java.util.HashMap;

public class ModelView {
    String url; /* url de destination */
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
    public ModelView(String url) {
        this.setUrl(url);
    }
    public ModelView() {
    }
    
    public void addObject(String variableName,Object o){
        this.data.put(variableName, o);
    }
    

}
