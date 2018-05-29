package com.johannesbrodwall.batchserver.batchfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BatchFileRepository {

    public List<BatchFile> list() {
        return Arrays.asList(new BatchFile());
    }

    public void save(String batchType, String submittedFileName, InputStream inputStream) throws IOException {
        Path tmpDir = Paths.get("tmp");
        Files.createDirectories(tmpDir);
        
        Path tmpFile = tmpDir.resolve("batchfile-" + LocalDate.now() + "-" + UUID.randomUUID());
        
        long fileLength = Files.copy(inputStream, tmpFile, StandardCopyOption.REPLACE_EXISTING);
    }

}
