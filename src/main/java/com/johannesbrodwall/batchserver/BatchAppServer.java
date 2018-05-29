package com.johannesbrodwall.batchserver;

import javax.servlet.MultipartConfigElement;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class BatchAppServer {
    
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(10080);
        tomcat.start();

        Context publicContext = tomcat.addContext("/public", 
                BatchAppServer.class.getResource("/webapp").getPath());
        publicContext.addChild(createDefaultServlet(publicContext));
        publicContext.addWelcomeFile("index.html");
        publicContext.addServletMappingDecoded("/", "default");
        
        Context apiContext = tomcat.addContext("/api", null);
        Tomcat.addServlet(apiContext, "batchServlet", new BatchServlet())
            .setMultipartConfigElement(new MultipartConfigElement(""));
        apiContext.addServletMappingDecoded("/batchFiles/*", "batchServlet");
        
        Context rootContext = tomcat.addContext("", null);
        Tomcat.addServlet(rootContext, "redirectServlet", new RedirectServlet("/public"));
        rootContext.addServletMappingDecoded("/", "redirectServlet");
        
        tomcat.getServer().await();
    }

    private static Wrapper createDefaultServlet(Context context) {
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName("default");
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        return defaultServlet;
    }

}
