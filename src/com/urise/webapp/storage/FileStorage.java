package com.urise.webapp.storage;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serialization.SerializationStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private File directory;
    private final SerializationStrategy serializationStrategy;

    protected FileStorage(File directory, SerializationStrategy serializationStrategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        this.serializationStrategy = serializationStrategy;
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writeable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        for (File resumeFile : getFilesList()) {
            doDelete(resumeFile);
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
            doUpdate(file, resume);
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getAbsolutePath(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File is not deleted");
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return serializationStrategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File is not found", file.getAbsolutePath(), e);
        }
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            serializationStrategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File is not written", file.getAbsolutePath(), e);
        }
    }

    @Override
    public int size() {
        return getFilesList().length;
    }

    @Override
    protected boolean isFound(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> getResumeList() {
        List<Resume> resumeList = new ArrayList<>();
        for (File resumeFile : getFilesList()) {
            resumeList.add(doGet(resumeFile));
        }
        return resumeList;
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    private File[] getFilesList() {
        File[] resumeFiles = directory.listFiles();
        if (resumeFiles == null) {
            throw new StorageException("Directory can't be read");
        }
        return resumeFiles;
    }

}
