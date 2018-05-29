package com.johannesbrodwall.batchserver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.sql.DataSource;

import com.johannesbrodwall.batchserver.batchfile.BatchFileRepository;
import com.johannesbrodwall.batchserver.infrastructure.sql.DataSourceImpl;

public class BatchServerContext {
    
    private DataSourceImpl dataSource = new DataSourceImpl();

    public Supplier<BatchFileRepository> batchFileRepository() {
        return () -> new BatchFileRepository(getDataSource());
    }

    private DataSource getDataSource() {
        dataSource.setProperties(getDataSourceProperties());
        return dataSource;
    }

    private Map<String, String> getDataSourceProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("url", "jdbc:h2:file:./target/dev.db");
        map.put("user", "sa");
        map.put("password", "");
        return map;
    }

}
