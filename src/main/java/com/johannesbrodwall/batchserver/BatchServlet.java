package com.johannesbrodwall.batchserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.JsonObject;

public class BatchServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonArray result = new JsonArray()
                .add(new JsonObject()
                    .put("id", 1)
                    .put("batchtype", "Fest")
                    .put("startTime", Instant.now())
                    .put("status", "ok")
                    .put("progress", "90%"));
        resp.setContentType("application/json");
        result.toJson(resp.getWriter());
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Part part = req.getPart("batchfile");
        
        Path tmpDir = Paths.get("tmp");
        Files.createDirectories(tmpDir);
        Path tmpFile = tmpDir.resolve("batchfile-" + LocalDate.now() + "-" + UUID.randomUUID());
        
        long fileLength = Files.copy(part.getInputStream(), tmpFile, StandardCopyOption.REPLACE_EXISTING);
        String batchtype = req.getParameter("batchtype");
        
        resp.sendRedirect("/");
    }

}
