package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Sorted array based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> RESUME_COMPARATOR =
            Comparator.comparing(Resume::getUuid);

    @Override
    protected Integer findSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "fullName");
        return Arrays.binarySearch(storage, 0, size(), searchKey, RESUME_COMPARATOR);
    }

    @Override
    protected void insertToStorage(Resume resume, int index) {
        int indexToInsert = -index - 1;
        vacateSpaceForInsert(indexToInsert);
        storage[indexToInsert] = resume;
    }

    private void vacateSpaceForInsert(int indexToVacate) {
        if (size() - 1 - indexToVacate >= 0)
            System.arraycopy(storage, indexToVacate, storage, indexToVacate + 1, size() - indexToVacate);
    }

    @Override
    protected void removeFromStorage(int index) {
        System.arraycopy(storage, index + 1, storage, index, size() - index);
    }

}
