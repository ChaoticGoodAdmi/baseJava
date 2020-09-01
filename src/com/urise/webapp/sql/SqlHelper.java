package com.urise.webapp.sql;

import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T executeReturnable(String sqlQuery, SqlQueryProcessor<T> processor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            return processor.getQueryResult(ps);
        } catch (SQLException e) {
            throwAppropriateException(e);
            return null;
        }
    }

    public void execute(String sqlQuery, SqlQueryExecutor executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            executor.execute(ps);
        } catch (SQLException e) {
            throwAppropriateException(e);
        }
    }

    private void throwAppropriateException(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            throw new ExistStorageException(null);
        }
        throw new StorageException(e);
    }
}
