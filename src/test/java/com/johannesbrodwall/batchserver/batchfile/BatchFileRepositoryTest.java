package com.johannesbrodwall.batchserver.batchfile;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import com.johannesbrodwall.batchserver.BatchServerSampleData;

public class BatchFileRepositoryTest {

    private BatchServerSampleData sampleData = new BatchServerSampleData();
    private BatchFileRepository repository = new BatchFileRepository(testDataSource());

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

    private DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
        
        return dataSource;
    }
    
    
}
