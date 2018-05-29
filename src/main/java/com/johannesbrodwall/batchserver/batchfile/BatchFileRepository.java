package com.johannesbrodwall.batchserver.batchfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import lombok.SneakyThrows;

public class BatchFileRepository {

    private DataSource dataSource;

    public BatchFileRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows(SQLException.class)
    public List<BatchFile> list() {
        List<BatchFile> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from batch_files")) {
                try(ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        result.add(mapBatchFile(rs));
                    }
                }
            }
        }
        return result;
    }

    public void save(String batchType, String submittedFileName, InputStream inputStream) throws IOException {
        Path tmpDir = Paths.get("tmp");
        Files.createDirectories(tmpDir);
        
        Path tmpFile = tmpDir.resolve("batchfile-" + LocalDate.now() + "-" + UUID.randomUUID());
        
        long fileLength = Files.copy(inputStream, tmpFile, StandardCopyOption.REPLACE_EXISTING);
        
        BatchFile batchFile = new BatchFile();
//        batchFile.setFileLocation(tmpFile);
//        batchFile.setFileLength(fileLength);
//        batchFile.setUploadTime(Instant.now());
//        batchFile.setSubmittedFileName(submittedFileName);
        
        save(batchFile);
    }

    @SneakyThrows(SQLException.class)
    public void save(BatchFile file) {
        file.setId(UUID.randomUUID());
        
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into batch_files (id) values (?)")) {
                statement.setObject(1, file.getId());
                
                statement.executeUpdate();
            }
        }
    }

    @SneakyThrows(SQLException.class)
    public BatchFile retrieve(UUID id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from batch_files where id = ?")) {
                statement.setObject(1, id);
                
                try(ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapBatchFile(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private BatchFile mapBatchFile(ResultSet rs) throws SQLException {
        BatchFile batchFile = new BatchFile();
        batchFile.setId(UUID.fromString(rs.getString("id")));
        return batchFile;
    }

}
