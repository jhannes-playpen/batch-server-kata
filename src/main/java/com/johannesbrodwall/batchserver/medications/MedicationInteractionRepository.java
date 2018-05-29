package com.johannesbrodwall.batchserver.medications;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.johannesbrodwall.batchserver.infrastructure.sql.AbstractSqlRepository;

public class MedicationInteractionRepository extends AbstractSqlRepository {

    public MedicationInteractionRepository(DataSource dataSource) {
        super(dataSource);
    }

    public List<MedicationInteraction> list() {
        return queryForList("select * from medication_interactions", this::mapRow);
    }
    
    private MedicationInteraction mapRow(ResultSet rs) throws SQLException {
        MedicationInteraction result = new MedicationInteraction();
        result.setId(rs.getString("id"));
        return result;
    }
    
    public void deleteAll() {
        // TODO Auto-generated method stub
        
    }

    public void save(MedicationInteraction interaction) {
        executeUpdate(
                "insert into medication_interactions (id) values (?)",
                interaction.getId());
    }

}
