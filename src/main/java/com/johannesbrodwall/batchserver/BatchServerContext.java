package com.johannesbrodwall.batchserver;

import java.util.function.Supplier;

import com.johannesbrodwall.batchserver.batchfile.BatchFileRepository;

public class BatchServerContext {

    public Supplier<BatchFileRepository> batchFileRepository() {
        return () -> new BatchFileRepository();
    }

}
