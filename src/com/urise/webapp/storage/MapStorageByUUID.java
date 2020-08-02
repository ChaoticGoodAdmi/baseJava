package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorageByUUID extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

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
    public List<Resume> getResumeList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void doUpdate(Object key, Resume resume) {
        storage.put(key.toString(), resume);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected Object findSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid).getUuid();
        } else return null;
    }

    protected boolean isFound(Object key) {
        return key != null;
    }
}