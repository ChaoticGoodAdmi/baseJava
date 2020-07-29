package com.urise.webapp.storage;

import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume r) {
        checkAbsence(r);
        doSave(r);
    }

    @Override
    public void delete(String uuid) {
        Object key = getKeyIfExistent(uuid);
        doDelete(key);
    }

    @Override
    public Resume get(String uuid) {
        Object key = getKeyIfExistent(uuid);
        return doGet(key);
    }

    @Override
    public void update(Resume r) {
        Object key = getKeyIfExistent(r.getUuid());
        doUpdate(key, r);
    }

    private void checkAbsence(Resume resume) {
        Object index = find(resume.getUuid());
        if (isFound(index)) {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    private Object getKeyIfExistent(String uuid) {
        Object index = find(uuid);
        if (!isFound(index)) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    public abstract int size();

    protected abstract void doUpdate(Object key, Resume resume);

    protected abstract void doSave(Resume resume);

    protected abstract Resume doGet(Object key);

    protected abstract void doDelete(Object key);

    protected abstract Object find(String uuid);

    protected abstract boolean isFound(Object key);

}
