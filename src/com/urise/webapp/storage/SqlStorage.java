package com.urise.webapp.storage;

import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executeReturnable("DELETE FROM resume",
                PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.execute(
                "INSERT INTO resume (uuid, full_name) VALUES (?, ?)",
                ps -> {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, r.getFullName());
                    ps.execute();
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute(
                "DELETE FROM resume WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    if (!ps.execute()) {
                        throw new NotExistStorageException(uuid);
                    }
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeReturnable(
                "SELECT * FROM resume r WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name"));
                }
        );
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeReturnable(
                "SELECT * FROM resume ORDER BY full_name, uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (rs.next()) {
                        resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
                    }
                    return resumes;
                }
        );
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute(
                "UPDATE resume SET full_name = ? WHERE uuid = ?",
                ps -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(r.getUuid());
                    }
                }
        );
    }

    @Override
    public int size() {
        return sqlHelper.executeReturnable(
                "SELECT COUNT(*) FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    return rs.next() ? rs.getInt("COUNT") : 0;
                }
        );
    }
}
