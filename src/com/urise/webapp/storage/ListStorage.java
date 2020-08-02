package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r, Object index) {
        storage.add(r);
    }

    @Override
    public void doDelete(Object key) {
        storage.remove((int) key);
    }

    @Override
    public Resume doGet(Object key) {
        return storage.get((int) key);
    }

    @Override
    public List<Resume> getResumeList() {
        return storage;
    }

    @Override
    public void doUpdate(Object key, Resume resume) {
        storage.set((Integer) key, resume);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected Object findSearchKey(String uuid) {
        Resume resume = new Resume(uuid, "fullName");
        return storage.indexOf(resume);
    }

    protected boolean isFound(Object key) {
        return (int) key >= 0;
    }

}
