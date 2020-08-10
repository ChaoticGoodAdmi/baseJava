package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorageByUuid extends AbstractStorage<String> {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r, String searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    public void doDelete(String key) {
        storage.remove(key);
    }

    @Override
    public Resume doGet(String key) {
        return storage.get(key);
    }

    @Override
    public List<Resume> getResumeList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void doUpdate(String key, Resume resume) {
        storage.put(key, resume);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected String findSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid).getUuid();
        } else return null;
    }

    protected boolean isFound(String key) {
        return key != null;
    }
}