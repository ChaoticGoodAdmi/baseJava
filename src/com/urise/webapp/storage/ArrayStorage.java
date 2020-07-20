package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findIndex(String uuid) {
        for (int i = 0; i < storageSize; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertToStorage(Resume resume, int index) {
        storage[storageSize] = resume;
    }

    @Override
    protected void removeFromStorage(int index) {
        storage[index] = storage[storageSize - 1];
        storage[storageSize - 1] = null;
    }

}
