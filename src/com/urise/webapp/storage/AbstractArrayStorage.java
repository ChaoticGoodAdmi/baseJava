package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    protected static final int MAX_SIZE = 100000;
    public Resume[] storage = new Resume[MAX_SIZE];
    protected int storageSize = 0;

    public void clear() {
        Arrays.fill(storage, 0, storageSize, null);
        storageSize = 0;
    }

    public void save(Resume resume) {
        if (storageSize == MAX_SIZE) {
            System.out.println("Добавление невозможно - хранилище переполнено");
        } else if (findIndex(resume.getUuid()) < 0) {
            insertToStorage(resume);
            storageSize++;
        } else {
            System.out.println("Резюме " + resume.getUuid() + " уже добавлено");
        }
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            if (index == storageSize - 1) {
                storage[index] = null;
            } else {
                if (storageSize - index >= 0) {
                    System.arraycopy(storage, index + 1, storage, index, storageSize - index);
                }
            }
            storageSize--;
        } else {
            System.out.println("Не найдено резюме " + uuid);
        }
    }

    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("Не найдено резюме " + resume.getUuid());
        }
    }


    public Resume[] getAll() {
        Resume[] resumes = new Resume[storageSize];
        System.arraycopy(storage, 0, resumes, 0, storageSize);
        return resumes;
    }

    public int size() {
        return storageSize;
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            return null;
        }
        return storage[index];
    }

    protected abstract int findIndex(String uuid);

    protected abstract void insertToStorage(Resume resume);
}
