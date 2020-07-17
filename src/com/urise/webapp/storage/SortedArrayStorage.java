package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Sorted array based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, storageSize, searchKey);
    }

    @Override
    protected void insertToStorage(Resume resume) {
        int index = storageSize;
        for (int i = 0; i < storageSize; i++) {
            if (storage[i].compareTo(resume) > 0) {
                index = i;
            }
        }
        vacateSpaceForInsert(index);
        storage[index] = resume;
    }

    private void vacateSpaceForInsert(int indexToVacate) {
        if (storageSize - 1 - indexToVacate >= 0)
            System.arraycopy(storage, indexToVacate, storage, indexToVacate + 1, storageSize - indexToVacate);
    }


}
