package com.johannesbrodwall.batchserver.medications;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import com.johannesbrodwall.batchserver.BatchServerSampleData;
import com.johannesbrodwall.batchserver.TestDataSource;

public class MedicationInteractionRepositoryTest {
    
    private MedicationInteractionRepository repository = new MedicationInteractionRepository(TestDataSource.testDataSource());
    
    @Test
    @Ignore
    public void shouldListSavedInteractions() {
        MedicationInteraction interaction = new BatchServerSampleData().sampleMedicationInteraction();
        
        assertThat(interaction).hasNoNullFieldsOrProperties();
        repository.save(interaction);
        assertThat(repository.list()).contains(interaction);
    }

}
