package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorageByResume extends AbstractStorage<Resume> {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r, Resume searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    public void doDelete(Resume resume) {
        storage.remove(resume.getUuid());
    }

    @Override
    public Resume doGet(Resume resume) {
        return resume;
    }

    @Override
    public List<Resume> getResumeList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void doUpdate(Resume resume, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected Resume findSearchKey(String uuid) {
        return storage.get(uuid);
    }

    protected boolean isFound(Resume resume) {
        return resume != null;
    }
}
