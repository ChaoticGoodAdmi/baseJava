package com.urise.webapp.storage;

import com.urise.webapp.exceptions.ExistStorageException;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public void save(Resume r) {
        LOG.info("Save " + r);
        SK searchKey = getKeyIfNonExistent(r);
        doSave(r, searchKey);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK key = getKeyIfExistent(uuid);
        doDelete(key);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK key = getKeyIfExistent(uuid);
        return doGet(key);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> resumeList = getResumeList();
        Collections.sort(resumeList);
        return resumeList;
    }

    @Override
    public void update(Resume r) {
        LOG.info("Update " + r);
        SK key = getKeyIfExistent(r.getUuid());
        doUpdate(key, r);
    }

    private SK getKeyIfNonExistent(Resume resume) {
        SK searchKey = findSearchKey(resume.getUuid());
        if (isFound(searchKey)) {
            LOG.warning("Resume " + resume.getUuid() + " already exists in a storage");
            throw new ExistStorageException(resume.getUuid());
        }
        return searchKey;
    }

    private SK getKeyIfExistent(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (!isFound(searchKey)) {
            LOG.warning("Resume " + uuid + " does not exist in a storage");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void doUpdate(SK key, Resume resume);

    protected abstract void doSave(Resume resume, SK searchKey);

    protected abstract Resume doGet(SK key);

    protected abstract void doDelete(SK key);

    protected abstract List<Resume> getResumeList();

    protected abstract SK findSearchKey(String uuid);

    protected abstract boolean isFound(SK key);

}
