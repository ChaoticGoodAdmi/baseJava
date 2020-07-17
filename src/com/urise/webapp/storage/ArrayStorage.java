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
        Arrays.fill(storage, null);
        storageSize = 0;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < storageSize; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkPresence(String uuid) {
        return findIndex(uuid) != -1;
    }

    public void save(Resume resume) {
        System.out.println(checkPresence(resume.getUuid()));
        if (storageSize == maxSize) {
            System.out.println("Добавление невозможно - хранилище переполнено");
        } else if (!checkPresence(resume.getUuid())) {
            storage[storageSize] = resume;
            storageSize++;
        } else {
            System.out.println("Резюме с таким uuid уже добавлено");
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (!checkPresence(uuid)) {
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (checkPresence(uuid)) {
            if (index == storageSize - 1) {
                storage[index] = null;
            } else {
                storage[index] = storage[storageSize - 1];
                storage[storageSize - 1] = null;
            }
            storageSize--;
        } else {
            System.out.println("Не найдено такое резюме");
        }

    }

    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("Не найдено такое резюме");
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
}
