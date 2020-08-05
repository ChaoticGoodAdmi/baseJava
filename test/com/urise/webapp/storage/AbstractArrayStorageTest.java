package com.urise.webapp.storage;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    AbstractArrayStorageTest(AbstractArrayStorage arrayStorage) {
        super(arrayStorage);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        int maxSize = AbstractArrayStorage.MAX_SIZE;
        try {
            for (int i = storage.size() + 1; i <= maxSize; i++) {
                storage.save(new Resume("name"));
            }
        } catch (StorageException s) {
            fail("Overflowing thrown too early");
        }
        storage.save(new Resume("UUID_TO_MUCH"));
        assertEquals(maxSize, storage.size());
    }
}