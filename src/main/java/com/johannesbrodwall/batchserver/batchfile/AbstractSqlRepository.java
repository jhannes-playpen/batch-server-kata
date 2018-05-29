package com.johannesbrodwall.batchserver.batchfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import com.johannesbrodwall.batchserver.infrastructure.sql.RowMapper;

import lombok.SneakyThrows;

public abstract class AbstractSqlRepository {

    protected final DataSource dataSource;

    public AbstractSqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows(SQLException.class)
    protected <T> List<T> queryForList(String sql, RowMapper<T> mapper) {
        List<T> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        result.add(mapper.mapRow(rs));
                    }
                }
            }
        }
        return result;
    }

    @SneakyThrows(SQLException.class)
    protected <T> T queryForSingle(String sql, UUID id, RowMapper<T> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setObject(1, id);
                
                try(ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapper.mapRow(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

}