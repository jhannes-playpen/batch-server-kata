package com.johannesbrodwall.batchserver.medications;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.johannesbrodwall.batchserver.BatchServerSampleData;
import com.johannesbrodwall.batchserver.TestDataSource;

public class MedicationInteractionRepositoryTest {
    
    private MedicationInteractionRepository repository = new MedicationInteractionRepository(TestDataSource.testDataSource());
    private BatchServerSampleData sampleData = new BatchServerSampleData();
    
    @Test
    public void shouldRetrieveFullObject() {
        MedicationInteraction interaction = sampleData.sampleMedicationInteraction();
        assertThat(interaction).hasNoNullFieldsOrProperties();
        repository.save(interaction);
        assertThat(repository.retrieve(interaction.getId()))
            .isEqualToComparingFieldByField(interaction);
    }
    
    @Test
    public void shouldListSavedInteractions() {
        MedicationInteraction interaction = sampleData.sampleMedicationInteraction();
        repository.save(interaction);
        assertThat(repository.list()).contains(interaction);
    }

}
