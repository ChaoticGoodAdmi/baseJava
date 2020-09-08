package com.urise.webapp.storage;

import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

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
                    putSections(r, conn);
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
                    deleteContacts(r, conn);
                    putContacts(r, conn);
                    deleteSections(r, conn);
                    putSections(r, conn);
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
        Resume resume = sqlHelper.executeReturnable(
                "  SELECT * FROM resume r " +
                        "    WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name"));
                }
        );
        resume.setContacts(getContacts(uuid));
        resume.setSections(getSections(uuid));
        return resume;
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

    private Map<ContactType, String> getContacts(String uuid) {
        return sqlHelper.executeReturnable(
                "SELECT * " +
                        "   FROM contact" +
                        "  WHERE resume_uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    Map<ContactType, String> contactsMap = new EnumMap<>(ContactType.class);
                    while (rs.next()) {
                        String value = rs.getString("value");
                        if (value != null) {
                            ContactType type = ContactType.valueOf(rs.getString("type"));
                            contactsMap.put(type, value);
                        }
                    }
                    return contactsMap;
                });
    }

    private void putSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO section (resume_uuid, type, content) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                SectionType sectionType = e.getKey();
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        ps.setString(3, ((TextSection) e.getValue()).getText());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        String listToText = String.join("\\n", ((ListSection) e.getValue()).getList());
                        ps.setString(3, listToText);
                        break;
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private Map<SectionType, Section> getSections(String uuid) {
        return sqlHelper.executeReturnable(
                "SELECT * " +
                        "   FROM section" +
                        "  WHERE resume_uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    Map<SectionType, Section> sectionMap = new EnumMap<>(SectionType.class);
                    while (rs.next()) {
                        SectionType sectionType = SectionType.valueOf(rs.getString("type"));
                        switch (sectionType) {
                            case PERSONAL:
                            case OBJECTIVE:
                                sectionMap.put(sectionType, new TextSection(rs.getString("content")));
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                sectionMap.put(sectionType, new ListSection(
                                        Arrays.asList(rs.getString("content").split("\\\\n"))
                                ));
                                break;
                        }
                    }
                    return sectionMap;
                });
    }

    private void deleteDetails(Resume r, Connection conn, String sqlQuery) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void deleteContacts(Resume r, Connection conn) throws SQLException {
        deleteDetails(r, conn, "DELETE FROM contact WHERE resume_uuid = ?");
    }

    private void deleteSections(Resume r, Connection conn) throws SQLException {
        deleteDetails(r, conn, "DELETE FROM section WHERE resume_uuid = ?");
    }
}
