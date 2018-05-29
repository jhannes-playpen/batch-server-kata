package com.johannesbrodwall.batchserver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.sql.DataSource;

import com.johannesbrodwall.batchserver.batchfile.BatchFileRepository;
import com.johannesbrodwall.batchserver.infrastructure.sql.DataSourceImpl;
import com.johannesbrodwall.batchserver.medications.MedicationInteractionRepository;

public class BatchServerContext {
    
    private DataSourceImpl dataSource = new DataSourceImpl(getDataSourceProperties());

    public Supplier<BatchFileRepository> batchFileRepository() {
        return () -> new BatchFileRepository(getDataSource());
    }

    public Supplier<MedicationInteractionRepository> medicationInteractionRepository() {
        return () -> new MedicationInteractionRepository(getDataSource());
    }

    private DataSource getDataSource() {
        dataSource.setProperties(getDataSourceProperties());
        return dataSource;
    }

    private static Map<String, String> getDataSourceProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("url", "jdbc:h2:file:./target/dev.db");
        map.put("user", "sa");
        map.put("password", "");
        return map;
    }


}
