/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int maxVolume = 10000;
    private final int EXPANSION_RATE = 2;
    private Resume[] storage = new Resume[maxVolume];
    private int storageSize = 0;

    void clear () {
        for (int i = 0; i < storageSize; i++) {
            storage[i] = null;
        }
        storageSize = 0;
    }

    void save (Resume resume) {
        if (storageSize == maxVolume) {
            expandStorage();
        }
        if (findResumeID(resume.uuid) == -1) {
            storage[storageSize] = resume;
            storageSize++;
        } else {
            System.out.println("Резюме с таким uuid уже добавлено");
        }
    }

    private void expandStorage () {
        int expandedSize = this.storageSize * this.EXPANSION_RATE;
        Resume[] expandedStorage = new Resume[expandedSize];
        System.arraycopy(this.storage, 0, expandedStorage, 0, storageSize);
        this.storage = expandedStorage;
        maxVolume = expandedSize;
        System.out.println("Размер хранилища увеличен до " + maxVolume);
    }

    Resume get (String uuid) {
        int id = findResumeID(uuid);
        if (id == -1) {
            Resume dummyResume = new Resume();
            dummyResume.uuid = "< не найдено >";
            return dummyResume;
        } else {
            return storage[id];
        }
    }

    void delete (String uuid) {
        int id = findResumeID(uuid);
        if (id != -1) {
            if (id == storageSize - 1) {
                storage[id] = null;
            } else {
                storage[id] = storage[storageSize - 1];
                storage[storageSize - 1] = null;
            }
            storageSize--;
        } else {
            System.out.println("Не найдено такое резюме");
        }

    }

    private int findResumeID (String uuid) {
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
    Resume[] getAll () {
        Resume[] cleanResumeStorage = new Resume[storageSize];
        System.arraycopy(storage, 0, cleanResumeStorage, 0, storageSize);
        return cleanResumeStorage;
    }

    int size () {
        return this.storageSize;
    }
}
