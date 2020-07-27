package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public interface Storage {

    void clear();

    void save(Resume r);

    void delete(String uuid);

    Resume get(String uuid);

    Resume[] getAll();

    void update(Resume r);

    int size();

}
