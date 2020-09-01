package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.urise.webapp.ResumeTestData.createTestResume;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    protected final Storage storage;
    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();

    private static final Resume R_1 = createTestResume(UUID_1, "NAME_1");
    private static final Resume R_2 = createTestResume(UUID_2, "NAME_2");
    private static final Resume R_3 = createTestResume(UUID_3, "NAME_3");

    AbstractStorageTest(Storage storage) {
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
        assertSize(0);
    }

    @Test
    public void saveNonExistent() {
        int initialSize = storage.size();
        Resume r4 = new Resume(UUID.randomUUID().toString(), "NAME_4");
        storage.save(r4);
        assertSize(initialSize + 1);
        assertGet(r4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistent() {
        storage.save(R_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExistent() {
        int initialSize = storage.size();
        storage.delete(UUID_1);
        assertSize(initialSize - 1);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNonExistent() {
        storage.delete("UUID_NOT_THERE");
    }

    @Test
    public void updateExistent() {
        Resume r4 = new Resume(UUID_3, "NEW_NAME_3");
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
        List<Resume> actualResumes = storage.getAllSorted();
        assertEquals(3, actualResumes.size());
        assertEquals(actualResumes, Arrays.asList(R_1, R_2, R_3));
    }

    @Test
    public void getAllSortedFromEmptyStorage() {
        storage.clear();
        List<Resume> actualResumes = storage.getAllSorted();
        assertEquals(0, actualResumes.size());
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void getExistent() {
        assertGet(R_1);
        assertGet(R_2);
        assertGet(R_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNonExistent() {
        storage.get("UUID_NOT_THERE");
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}
