package com.johannesbrodwall.batchserver.batchfile;

import java.io.IOException;
import java.time.Instant;
import java.util.function.Supplier;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.JsonObject;

import com.johannesbrodwall.batchserver.BatchServerContext;

public class BatchServlet extends HttpServlet {
    
    private Supplier<BatchFileRepository> repository;

    public BatchServlet(BatchServerContext applicationContext) {
        this.repository = applicationContext.batchFileRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonArray result = JsonArray.map(repository.get().list(), f -> new JsonObject()
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
        repository.get().save(req.getParameter("batchtype"),
                part.getSubmittedFileName(),
                part.getInputStream());
        
        
        resp.sendRedirect("/");
    }

}
