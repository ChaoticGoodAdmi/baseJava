package com.urise.webapp.storage;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    private AbstractArrayStorage arrayStorage;

    AbstractArrayStorageTest(AbstractArrayStorage storage) {
        super(storage);
        arrayStorage = storage;
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        int maxSize = AbstractArrayStorage.MAX_SIZE;
        try {
            for (int i = arrayStorage.size() + 1; i <= maxSize; i++) {
                arrayStorage.save(new Resume());
            }
        } catch (StorageException s) {
            fail("Переполнение вызвано слишком рано");
        }
        arrayStorage.save(new Resume("UUID_TO_MUCH"));
        assertEquals(maxSize, arrayStorage.size());
    }
}