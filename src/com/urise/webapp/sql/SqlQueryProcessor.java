package com.urise.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlQueryProcessor<T> {

    T getQueryResult(PreparedStatement ps) throws SQLException;
}
