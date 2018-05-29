package com.johannesbrodwall.batchserver.batchfile;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.Test;

import com.johannesbrodwall.batchserver.BatchServerSampleData;
import com.johannesbrodwall.batchserver.TestDataSource;

public class BatchFileRepositoryTest {

    private BatchServerSampleData sampleData = new BatchServerSampleData();
    private BatchFileRepository repository = new BatchFileRepository(TestDataSource.testDataSource());

    @Test
    public void shouldListSavedBatchFile() {
        BatchFile file = sampleData.sampleBatchFile();
        assertThat(repository.list()).doesNotContain(file);

        repository.save(file);
        assertThat(repository.list()).contains(file);
    }

    @Test
    public void shouldRetrieveSavedBatchFile() {
        BatchFile file = sampleData.sampleBatchFile();
        assertThat(file).hasNoNullFieldsOrPropertiesExcept("id");
        
        repository.save(file);
        assertThat(file.getId()).isNotNull();
        assertThat(repository.retrieve(file.getId()))
            .isEqualToComparingFieldByField(file);
    }
    
    @Test
    public void shouldUpdateSavedBatchFile() {
        BatchFile file = sampleData.sampleBatchFile();
        repository.save(file);
        UUID id = file.getId();
        
        file.setStatus(BatchFile.Status.COMPLETE);
        file.setSubmittedFileName(sampleData.randomFileName());
        repository.save(file);
        assertThat(repository.retrieve(id)).isEqualToComparingFieldByField(file);
    }
}
