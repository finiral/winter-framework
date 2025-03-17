package mg.itu.prom16.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.annotations.Auth;
import mg.itu.prom16.annotations.AuthC;
import mg.itu.prom16.annotations.Controller;
import mg.itu.prom16.annotations.RestAPI;
import mg.itu.prom16.exceptions.ValidationException;
import mg.itu.prom16.object.ModelView;
import mg.itu.prom16.object.ResourceNotFound;
import mg.itu.prom16.object.VerbMethod;
import mg.itu.prom16.utils.Mapping;
import mg.itu.prom16.utils.Utils;

@MultipartConfig
public class FrontController extends HttpServlet {
    private List<String> controllers;
    private HashMap<String, Mapping> map;
    private String authVarName;
    private String authRoleVarName;

    @Override
    public void init() throws ServletException {
        String packageToScan = this.getInitParameter("package_name");
        if (packageToScan != null) {
            try {
                this.controllers = new Utils().getAllClassesStringAnnotation(packageToScan, Controller.class);
                this.map = new Utils().scanControllersMethods(this.controllers);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        this.authVarName = this.getInitParameter("auth_name");
        this.authRoleVarName = this.getInitParameter("auth_role_name");
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
        Object res = null;
        try {
            // Prendre les parametres
            Map<String, String[]> params = request.getParameterMap();
            // Recherche methode et verification auth
            VerbMethod meth = u.searchVerbMethod(request, map, path, this.authVarName, this.authRoleVarName);
            // Execution methode
            res = u.execute(request, meth, map, path, params);
            /* verification si methode est rest */
            if (meth.getMethode().isAnnotationPresent(RestAPI.class)) {
                /* Changer le type du response en json */
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Gson gson = new Gson();
                /* si le type de retour nest pas modelview on return le json directement */
                if (!(res instanceof ModelView)) {
                    gson.toJson(res, out);
                }
                /* si c'est model view */
                else {
                    ModelView mv = (ModelView) res;
                    gson.toJson(mv.getData(), out);
                }
            }
            /* si methode NON REST */
            else {
                out.println("L'URL EST :" + url);
                out.println("L'URL a chercher dans le map : " + path);
                /* Printer tous les controllers */
                out.print("\n");
                out.println("Liste de tous vos controllers : ");
                for (String class1 : this.controllers) {
                    out.println(class1);
                }
                if (res instanceof String) {
                    out.println(res.toString());
                } else if (res instanceof ModelView) {
                    ModelView modelview = (ModelView) res;
                    if (modelview.getUrl().startsWith("redirect:")) {
                        response.sendRedirect(request.getContextPath()+modelview.getUrl().substring(9));
                    } else {
                        String urlDispatch = modelview.getUrl();
                        RequestDispatcher dispatcher = request.getRequestDispatcher(urlDispatch);
                        HashMap<String, Object> data = modelview.getData();
                        for (String key : data.keySet()) {
                            request.setAttribute(key, data.get(key));
                        }
                        dispatcher.forward(request, response);
                    }
                }
            }
        } catch (ValidationException e) {
            String errorUrl = e.getErrorUrl();
            String errorMethod = e.getErrorMethod();
            if (errorUrl != null) {
                // Create a new HttpServletRequestWrapper to modify the request method
                HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getMethod() {
                        return errorMethod;
                    }
                };

                wrappedRequest.setAttribute("errors", e.getErrorMap());
                wrappedRequest.setAttribute("params", e.getParamsBeforeError());
                // Dispatch the new request to errorUrl
                RequestDispatcher dispatcher = wrappedRequest.getRequestDispatcher(errorUrl);
                dispatcher.forward(wrappedRequest, response);
            } else {
                // Print the errors directly
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
        } catch (ResourceNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            /* throw new ServletException(e); */
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            e.printStackTrace();
        }
    }
}