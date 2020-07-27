package com.urise.webapp.storage;

import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r) {
        storage.add(r);
    }

    @Override
    public void doDelete(Object key) {
        int index = (int) key;
        storage.remove(index);
    }

    @Override
    public Resume doGet(Object key) {
        int index = (int) key;
        return storage.get(index);
    }

    @Override
    public Resume[] getAll() {
        Resume[] r = new Resume[storage.size()];
        return storage.toArray(r);
    }

    @Override
    public void doUpdate(Object key, Resume resume) {
        storage.set((Integer) key, resume);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected void checkAbsence(Resume resume) {
        String uuid = resume.getUuid();
        if (storage.contains(resume)) {
            throw new ExistStorageException(uuid);
        }
    }

    protected Object getKeyIfExistent(String uuid) {
        Resume resume = new Resume(uuid);
        if (!storage.contains(new Resume(uuid))) {
            throw new NotExistStorageException(uuid);
        }
        return storage.indexOf(resume);
    }
}
