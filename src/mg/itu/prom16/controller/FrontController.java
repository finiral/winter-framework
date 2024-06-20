package mg.itu.prom16.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.annotations.Controller;
import mg.itu.prom16.object.ModelView;
import mg.itu.prom16.utils.Mapping;
import mg.itu.prom16.utils.Utils;

public class FrontController extends HttpServlet {
    private List<String> controllers;
    private HashMap<String, Mapping> map;

    @Override
    public void init() throws ServletException {
        String packageToScan = this.getInitParameter("package_name");
        try {
            this.controllers = new Utils().getAllClassesStringAnnotation(packageToScan, Controller.class);
            this.map = new Utils().scanControllersMethods(this.controllers);
        } catch (Exception e) {
            throw new ServletException(e);
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
        Utils u = new Utils();
        PrintWriter out = response.getWriter();
        StringBuffer url = request.getRequestURL();
        /* URL a rechercher dans le map */
        String path = u.getURIWithoutContextPath(request);
        out.println("L'URL EST :" + url);
        out.println("L'URL a chercher dans le map : " + path);
        /* Prendre le mapping correspondant a l'url */
        try {
            // Prendre les parametres
            Map<String, String[]> params = request.getParameterMap();
            Object res = u.searchExecute(map, path,params);
            if (res instanceof String) {
                out.println(res.toString());
            } else if (res instanceof ModelView) {
                ModelView modelview = (ModelView) res;
                String urlDispatch = modelview.getUrl();
                RequestDispatcher dispatcher = request.getRequestDispatcher(urlDispatch);
                HashMap<String, Object> data = modelview.getData();
                for (String key : data.keySet()) {
                    request.setAttribute(key, data.get(key));
                }
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            /* throw new ServletException(e); */
            e.printStackTrace(out);
        }
        /* Printer tous les controllers */
        out.print("\n");
        out.println("Liste de tous vos controllers : ");
        for (String class1 : this.controllers) {
            out.println(class1);
        }
    }
}