/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size = 0;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
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
        if (id == size - 1) {
            storage[id] = null;
        } else {
            storage[id] = storage[size - 1];
            storage[size - 1] = null;
        }
        size--;
    }

    private int findResumeID(String uuid) {
        for (int i = 0; i < size; i++) {
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
        Resume[] cvs = new Resume[size];
        System.arraycopy(storage, 0, cvs, 0, size);
        return cvs;
    }

    int size() {
        return this.size;
    }
}
