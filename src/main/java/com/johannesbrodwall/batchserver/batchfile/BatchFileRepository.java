package com.johannesbrodwall.batchserver.batchfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.IntPredicate;

import javax.sql.DataSource;

public class BatchFileRepository {

    private HashMap<UUID, BatchFile> values = new HashMap<>();

    public BatchFileRepository(DataSource dataSource) {
        // TODO Auto-generated constructor stub
    }

    public List<BatchFile> list() {
        return Arrays.asList(new BatchFile());
    }

    public void save(String batchType, String submittedFileName, InputStream inputStream) throws IOException {
        Path tmpDir = Paths.get("tmp");
        Files.createDirectories(tmpDir);
        
        Path tmpFile = tmpDir.resolve("batchfile-" + LocalDate.now() + "-" + UUID.randomUUID());
        
        long fileLength = Files.copy(inputStream, tmpFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void save(BatchFile file) {
        values.put(file.getId(), file);
    }

    public BatchFile retrieve(UUID id) {
        return values.get(id);
    }

}
