/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int storageSize = 0;

    void clear() {
        for (int i = 0; i < storageSize; i++) {
            storage[i] = null;
        }
        storageSize = 0;
    }

    void save(Resume resume) {
        storage[storageSize] = resume;
        storageSize++;
    }

    Resume get(String uuid) {
        int id = findResumeID(uuid);
        if (id == -1) {
            return null;
        } else {
            return storage[id];
        }
    }

    void delete(String uuid) {
        int id = findResumeID(uuid);
        if (id == storageSize - 1) {
            storage[id] = null;
        } else {
            storage[id] = storage[storageSize - 1];
            storage[storageSize - 1] = null;
        }
        storageSize--;
    }

    private int findResumeID(String uuid) {
        for (int i = 0; i < storageSize; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] cleanResumeStorage = new Resume[storageSize];
        System.arraycopy(storage, 0, cleanResumeStorage, 0, storageSize);
        return cleanResumeStorage;
    }

    int size() {
        return this.storageSize;
    }
}
