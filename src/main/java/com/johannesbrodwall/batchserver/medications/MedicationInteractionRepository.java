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
        result.setClinicalConsequence(rs.getString("clinical_consequence"));
        result.setInteractionMechanism(rs.getString("interaction_mechanism"));
        return result;
    }
    
    public void deleteAll() {
        executeUpdate("delete from medication_interactions");
    }

    public void save(MedicationInteraction interaction) {
        executeUpdate(
                "insert into medication_interactions (id, clinical_consequence, interaction_mechanism) values (?, ?, ?)",
                interaction.getId(),
                interaction.getClinicalConsequence(),
                interaction.getInteractionMechanism());
    }

    public MedicationInteraction retrieve(String id) {
        return queryForSingle("select * from medication_interactions where id = ?", id, this::mapRow);

    }

}
