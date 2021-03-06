package com.urise.webapp.storage;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    protected static final int MAX_SIZE = 10000;
    protected final Resume[] storage = new Resume[MAX_SIZE];
    private int storageSize = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, storageSize, null);
        storageSize = 0;
    }

    @Override
    public void doSave(Resume resume, Integer searchKey) {
        if (storageSize == MAX_SIZE) {
            throw new StorageException("Reached max size of a storage", resume.getUuid());
        } else {
            insertToStorage(resume, searchKey);
            storageSize++;
        }
    }

    @Override
    public void doDelete(Integer key) {
        int index = key;
        if (index == storageSize - 1) {
            storage[index] = null;
        } else if (storageSize - index >= 0) {
            removeFromStorage(index);
        }
        storage[storageSize - 1] = null;
        storageSize--;
    }

    @Override
    public Resume doGet(Integer key) {
        return storage[key];
    }

    @Override
    protected void doUpdate(Integer key, Resume resume) {
        storage[key] = resume;
    }

    @Override
    public int size() {
        return storageSize;
    }

    protected boolean isFound(Integer key) {
        return key >= 0;
    }

    @Override
    protected List<Resume> getResumeList() {
        Resume[] resumes = new Resume[storageSize];
        System.arraycopy(storage, 0, resumes, 0, storageSize);
        return Arrays.asList(resumes);
    }

    protected abstract void insertToStorage(Resume resume, int index);

    protected abstract void removeFromStorage(int index);
}
