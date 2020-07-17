package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int maxSize = 10000;
    private Resume[] storage = new Resume[maxSize];
    private int storageSize = 0;

    public void clear() {
        Arrays.fill(storage, 0, storageSize, null);
        storageSize = 0;
    }

    public void save(Resume resume) {
        if (storageSize == maxSize) {
            System.out.println("Добавление невозможно - хранилище переполнено");
        } else if (findIndex(resume.getUuid()) == -1) {
            storage[storageSize] = resume;
            storageSize++;
        } else {
            System.out.println("Резюме " + resume.getUuid() + " уже добавлено");
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index != -1) {
            if (index == storageSize - 1) {
                storage[index] = null;
            } else {
                storage[index] = storage[storageSize - 1];
                storage[storageSize - 1] = null;
            }
            storageSize--;
        } else {
            System.out.println("Не найдено резюме " + uuid);
        }

    }

    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("Не найдено резюме " + resume.getUuid());
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storageSize];
        System.arraycopy(storage, 0, resumes, 0, storageSize);
        return resumes;
    }

    public int size() {
        return storageSize;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < storageSize; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
