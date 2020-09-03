package com.urise.webapp.storage;

import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executeReturnable("DELETE FROM resume r",
                PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.executeTransactional(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    putContacts(r, conn);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute(
                "DELETE FROM resume r WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    if (!ps.execute()) {
                        throw new NotExistStorageException(uuid);
                    }
                });
    }

    @Override
    public Resume get(String uuid) {
        return getResume(uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeReturnable(
                "SELECT * FROM resume ORDER BY full_name, uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (rs.next()) {
                        Resume resume = getResume(rs.getString("uuid"));
                        resumes.add(resume);
                    }
                    return resumes;
                }
        );
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeTransactional(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, r.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(r.getUuid());
                        }
                    }
                    deleteContacts(r);
                    putContacts(r, conn);
                    return null;
                }
        );
    }

    @Override
    public int size() {
        return sqlHelper.executeReturnable(
                "SELECT COUNT(*) FROM resume r",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    return rs.next() ? rs.getInt("COUNT") : 0;
                }
        );
    }

    private Resume getResume(String uuid) {
        return sqlHelper.executeReturnable(
                "  SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "       ON r.uuid = c.resume_uuid " +
                        "    WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    Map<ContactType, String> contacts = getContacts(rs);
                    r.setContacts(contacts);
                    return r;
                }
        );
    }

    private void putContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private Map<ContactType, String> getContacts(ResultSet rs) throws SQLException {
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        do {
            String value = rs.getString("value");
            if (value != null) {
                ContactType type = ContactType.valueOf(rs.getString("type"));
                contacts.put(type, value);
            }
        } while (rs.next());
        return contacts;
    }

    private void deleteContacts(Resume r) {
        sqlHelper.execute("DELETE FROM contact WHERE resume_uuid = ?",
                ps -> {
                    ps.setString(1, r.getUuid());
                    ps.execute();
                });
    }
}
