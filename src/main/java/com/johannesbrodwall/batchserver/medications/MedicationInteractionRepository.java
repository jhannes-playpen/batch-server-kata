package com.johannesbrodwall.batchserver.medications;

import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import com.johannesbrodwall.batchserver.batchfile.AbstractSqlRepository;

public class MedicationInteractionRepository extends AbstractSqlRepository {

    public MedicationInteractionRepository(DataSource dataSource) {
        super(dataSource);
    }

    public List<MedicationInteraction> list() {
        return queryForList("select * from medication_interactions", this::mapRow);
    }
    
    private MedicationInteraction mapRow(ResultSet rs) {
        return new MedicationInteraction();
    }
    
    public void deleteAll() {
        // TODO Auto-generated method stub
        
    }

}
