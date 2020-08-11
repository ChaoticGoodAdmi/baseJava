package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {

    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r, Integer searchKey) {
        storage.add(r);
    }

    @Override
    public void doDelete(Integer key) {
        storage.remove(key.intValue());
    }

    @Override
    public Resume doGet(Integer key) {
        return storage.get(key);
    }

    @Override
    public List<Resume> getResumeList() {
        return storage;
    }

    @Override
    public void doUpdate(Integer key, Resume resume) {
        storage.set(key, resume);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected Integer findSearchKey(String uuid) {
        for (Resume resume : storage) {
            if (resume.getUuid().equals(uuid))
                return storage.indexOf(resume);
        }
        return -1;
    }

    protected boolean isFound(Integer key) {
        return key >= 0;
    }

}
