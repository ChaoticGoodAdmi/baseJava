package com.urise.webapp.storage;

import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {

    protected final Storage storage;
    private static final String UUID_1 = "UUID_1";
    private static final String UUID_2 = "UUID_2";
    private static final String UUID_3 = "UUID_3";

    private static final Resume R_1 = new Resume(UUID_1, "NAME_1");
    private static final Resume R_2 = new Resume(UUID_2, "NAME_2");
    private static final Resume R_3 = new Resume(UUID_3, "NAME_3");

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
        assertEquals(0, storage.size());
    }

    @Test
    public void saveNonExistent() {
        int initialSize = storage.size();
        Resume r4 = new Resume("UUID_4", "NAME_4");
        storage.save(r4);
        assertEquals(initialSize + 1, storage.size());
        assertEquals(r4, storage.get(r4.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistent() {
        storage.save(R_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExistent() {
        int initialSize = storage.size();
        storage.delete(UUID_1);
        assertEquals(initialSize - 1, storage.size());
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
        assertEquals(Arrays.asList(R_1, R_2, R_3), actualResumes);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void getExistent() {
        Resume r = storage.get(R_2.getUuid());
        assertEquals(R_2, r);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNonExistent() {
        final String UUID_NOT_THERE = "UUID_NOT_THERE";
        storage.get(UUID_NOT_THERE);
    }

}
