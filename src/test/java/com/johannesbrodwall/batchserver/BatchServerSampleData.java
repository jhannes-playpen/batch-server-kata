package com.johannesbrodwall.batchserver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import com.johannesbrodwall.batchserver.batchfile.BatchFile;

public class BatchServerSampleData {
    
    private Path tmpDir = Paths.get("target", "tmp");
    private Random random = new Random();

    public BatchFile sampleBatchFile() {
        BatchFile batchFile = new BatchFile();
        batchFile.setFileLength(100L);
        batchFile.setFileLocation(sampleFilePath());
        batchFile.setSubmittedFileName(UUID.randomUUID() + ".xml.gz");
        batchFile.setUploadTime(samplePastInstant());
        return batchFile;
    }

    private Instant samplePastInstant() {
        return Instant.now().minusSeconds(random.nextInt(10_000));
    }

    private Path sampleFilePath() {
        return tmpDir.resolve(UUID.randomUUID().toString());
    }

}
