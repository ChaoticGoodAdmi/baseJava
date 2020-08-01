package com.urise.webapp.storage;

import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume r) {
        Object index = checkAbsence(r);
        doSave(r, index);
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

    private Object checkAbsence(Resume resume) {
        Object searchKey = findSearchKey(resume.getUuid());
        if (isFound(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        }
        return searchKey;
    }

    private Object getKeyIfExistent(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (!isFound(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void doUpdate(Object key, Resume resume);

    protected abstract void doSave(Resume resume, Object index);

    protected abstract Resume doGet(Object key);

    protected abstract void doDelete(Object key);

    protected abstract Object findSearchKey(String uuid);

    protected abstract boolean isFound(Object key);

}
