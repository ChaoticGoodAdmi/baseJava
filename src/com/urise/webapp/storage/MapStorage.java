package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r, Object index) {
        storage.put(r.getUuid(), r);
    }

    @Override
    public void doDelete(Object key) {
        storage.remove(key.toString());
    }

    @Override
    public Resume doGet(Object key) {
        return storage.get(key.toString());
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.values().toArray(resumes);
    }

    @Override
    public void doUpdate(Object key, Resume resume) {
        storage.put(key.toString(), resume);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected Object findIndex(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid).getUuid();
        } else return null;
    }

    protected boolean isFound(Object key) {
        return key != null;
    }
}
