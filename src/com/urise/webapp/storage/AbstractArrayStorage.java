package com.urise.webapp.storage;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int MAX_SIZE = 10000;
    Resume[] storage = new Resume[MAX_SIZE];
    int storageSize = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, storageSize, null);
        storageSize = 0;
    }

    @Override
    public void doSave(Resume resume) {
        checkAbsence(resume);
        if (storageSize == MAX_SIZE) {
            throw new StorageException(resume.getUuid(), "Хранилище переполнено");
        } else {
            insertToStorage(resume, findIndex(resume.getUuid()));
            storageSize++;
        }
    }

    @Override
    public void doDelete(Object key) {
        int index = (int) key;
        if (index == storageSize - 1) {
            storage[index] = null;
        } else if (storageSize - index >= 0) {
            removeFromStorage(index);
        }
        storage[storageSize - 1] = null;
        storageSize--;
    }

    @Override
    public Resume doGet(Object key) {
        int index = (int) key;
        return storage[index];
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storageSize];
        System.arraycopy(storage, 0, resumes, 0, storageSize);
        return resumes;
    }

    @Override
    protected void doUpdate(Object key, Resume resume) {
        int index = (int) key;
        storage[index] = resume;
    }

    @Override
    public int size() {
        return storageSize;
    }

    protected Object find(String uuid) {
        return findIndex(uuid);
    }

    protected boolean isFound(Object key) {
        return (int) key >= 0;
    }

    protected abstract int findIndex(String uuid);

    protected abstract void insertToStorage(Resume resume, int index);

    protected abstract void removeFromStorage(int index);
}
