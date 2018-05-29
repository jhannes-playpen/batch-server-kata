package com.johannesbrodwall.batchserver.batchfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import com.johannesbrodwall.batchserver.infrastructure.sql.AbstractSqlRepository;

public class BatchFileRepository extends AbstractSqlRepository {

    public BatchFileRepository(DataSource dataSource) {
        super(dataSource);
    }

    public List<BatchFile> list() {
        return queryForList("select * from batch_files", this::mapBatchFile);
    }

    public BatchFile save(String batchType, String submittedFileName, InputStream inputStream) throws IOException {
        Path tmpDir = Paths.get("tmp");
        Files.createDirectories(tmpDir);
        
        Path tmpFile = tmpDir.resolve("batchfile-" + LocalDate.now() + "-" + UUID.randomUUID());
        
        long fileLength = Files.copy(inputStream, tmpFile, StandardCopyOption.REPLACE_EXISTING);
        
        BatchFile batchFile = new BatchFile();
        batchFile.setFileLocation(tmpFile);
        batchFile.setFileLength(fileLength);
        batchFile.setUploadTime(Instant.now());
        batchFile.setSubmittedFileName(submittedFileName);
        
        save(batchFile);
        
        return batchFile;
    }

    public void save(BatchFile file) {
        file.setId(UUID.randomUUID());
        
        executeUpdate(
                "insert into batch_files (id, file_location, file_length, submitted_filename, upload_time) values (?, ?, ?, ?, ?)",
                file.getId().toString(),
                file.getFileLocation().toString(),
                file.getFileLength(),
                file.getSubmittedFileName(),
                file.getUploadTime());
    }

    public BatchFile retrieve(UUID id) {
        return queryForSingle("select * from batch_files where id = ?", id, this::mapBatchFile);
    }

    private BatchFile mapBatchFile(ResultSet rs) throws SQLException {
        BatchFile batchFile = new BatchFile();
        batchFile.setId(UUID.fromString(rs.getString("id")));
        batchFile.setFileLocation(Paths.get(rs.getString("file_location")));
        batchFile.setFileLength(rs.getLong("file_length"));
        batchFile.setSubmittedFileName(rs.getString("submitted_filename"));
        batchFile.setUploadTime(rs.getTimestamp("upload_time").toInstant());
        return batchFile;
    }

}
