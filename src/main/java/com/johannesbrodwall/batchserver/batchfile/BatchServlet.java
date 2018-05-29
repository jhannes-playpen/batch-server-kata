package com.johannesbrodwall.batchserver.batchfile;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.jsonbuddy.JsonArray;
import org.jsonbuddy.JsonObject;

import com.johannesbrodwall.batchserver.BatchServerContext;
import com.johannesbrodwall.batchserver.medications.FestFileBatch;

public class BatchServlet extends HttpServlet {
   
    private Executor executor = Executors.newFixedThreadPool(10);
    
    private Supplier<BatchFileRepository> repository;

    private BatchServerContext applicationContext;

    public BatchServlet(BatchServerContext applicationContext) {
        this.applicationContext = applicationContext;
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
        BatchFile batchFile = repository.get().save(req.getParameter("batchtype"),
                part.getSubmittedFileName(),
                part.getInputStream());
        
        executor.execute(new FestFileBatch(batchFile.getId(), applicationContext));
        
        
        resp.sendRedirect("/");
    }

}
