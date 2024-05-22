package mg.itu.prom16.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.annotations.Controller;
import mg.itu.prom16.utils.Mapping;
import mg.itu.prom16.utils.Utils;

public class FrontController extends HttpServlet {
    private List<String> controllers;   
    private HashMap<String,Mapping> map;


    
    @Override
    public void init() throws ServletException {
        super.init();
        String packageToScan = this.getInitParameter("package_name");
        try {
            this.controllers=new Utils().getAllClassesStringAnnotation(packageToScan,Controller.class);
            this.map=new Utils().scanControllersMethods(this.controllers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        StringBuffer url = request.getRequestURL();
        out.println("L'URL EST :" + url);
        /* Prendre le mapping correspondant a l'url */
        if(map.containsKey(url.toString())){
            Mapping m=map.get(url.toString());
            out.println("Nom de la classe : "+ m.getClassName());
            out.println("Nom de la méthode : "+ m.getMethodName());
        }
        else{
            out.println("Aucune méthode associé a cette url");
        }
        /* Printer tous les controllers */
        for (String class1 : controllers) {
            out.println(class1);
        }
    }
}