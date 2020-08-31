package com.urise.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlQueryExecutor<T> {

    T execute(PreparedStatement ps) throws SQLException;
}
