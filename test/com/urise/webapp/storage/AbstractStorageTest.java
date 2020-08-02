package com.urise.webapp.storage;

import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractStorageTest {

    AbstractStorage storage;
    private static final String UUID_1 = "UUID_1";
    private static final String UUID_2 = "UUID_2";
    private static final String UUID_3 = "UUID_3";

    private static final String NAME_1 = "NAME_1";
    private static final String NAME_2 = "NAME_2";
    private static final String NAME_3 = "NAME_3";

    private static final Resume R_1 = new Resume(UUID_1, NAME_1);
    private static final Resume R_2 = new Resume(UUID_2, NAME_2);
    private static final Resume R_3 = new Resume(UUID_3, NAME_3);

    AbstractStorageTest(AbstractStorage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(R_3);
        storage.save(R_2);
        storage.save(R_1);
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void saveNonExistent() {
        int initialSize = storage.size();
        Resume r4 = new Resume("UUID_4", "NAME_4");
        storage.save(r4);
        assertEquals(initialSize + 1, storage.size());
        assertEquals(storage.get(r4.getUuid()), r4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistent() {
        storage.save(R_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExistent() {
        int initialSize = storage.size();
        storage.delete(R_1.getUuid());
        assertEquals(initialSize - 1, storage.size());
        storage.get(R_1.getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNonExistent() {
        storage.delete("UUID_NOT_THERE");
    }

    @Test
    public void updateExistent() {
        Resume r4 = new Resume(UUID_3, NAME_3);
        storage.update(r4);
        assertEquals(r4, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNonExistent() {
        Resume r4 = new Resume("UUID_NOT_THERE");
        storage.update(r4);
    }

    @Test
    public void getAllSorted() {
        List<Resume> resumes = storage.getAllSorted();
        assertEquals(3, resumes.size());
        assertTrue(assertContainment(resumes, R_1));
        assertTrue(assertContainment(resumes, R_2));
        assertTrue(assertContainment(resumes, R_3));
    }

    @Test
    public void assertSorting() {
        List<Resume> resumes = storage.getAllSorted();
        for (int i = 1; i < resumes.size(); i++) {
            assertTrue(resumes.get(i).compareTo(resumes.get(i-1)) >= 0);
        }
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void getExistent() {
        Resume r = storage.get(R_2.getUuid());
        assertEquals(r, R_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNonExistent() {
        storage.get("UUID_NOT_THERE");
    }

    private boolean assertContainment(List<Resume> resumes, Resume resume) {
        for (Resume r : resumes) {
            if (r.equals(resume)) {
                return true;
            }
        }
        return false;
    }
}
