package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Sorted array based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, storageSize, searchKey);
    }

    @Override
    protected void insertToStorage(Resume resume, int index) {
        int indexToInsert = -index - 1;
        vacateSpaceForInsert(indexToInsert);
        storage[indexToInsert] = resume;
    }

    private void vacateSpaceForInsert(int indexToVacate) {
        if (storageSize - 1 - indexToVacate >= 0)
            System.arraycopy(storage, indexToVacate, storage, indexToVacate + 1, storageSize - indexToVacate);
    }


}
