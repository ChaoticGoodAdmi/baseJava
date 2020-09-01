package com.urise.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlQueryExecutor {

    void execute(PreparedStatement ps) throws SQLException;
}
