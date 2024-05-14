package mg.itu.prom16.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.utils.Controller;

public class FrontController extends HttpServlet {
    private List<Class<?>> controllers;
    private boolean isChecked;


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
        if(!this.isChecked){
            String packageToScan = this.getInitParameter("package_name");
            try {
                this.controllers=this.getAllControllers(packageToScan);
                this.isChecked=true;
                out.println("Premier et dernier scan");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /* Printer tous les controllers */
        for (Class<?> class1 : controllers) {
            out.println(class1.getName());
        }
    }



    boolean isController(Class<?> c) {
        return c.isAnnotationPresent(Controller.class);
    }

    List<Class<?>> getAllControllers(String packageName) throws Exception {
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
                    if (this.isController(classe)) {
                        res.add(classe);
                    }
                }
            }
        }
        return res;

    }
}