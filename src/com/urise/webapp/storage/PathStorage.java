package com.urise.webapp.storage;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.Resume;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private Path directory;
    private SerializationStrategy serializationStrategy;

    public PathStorage(String dir, SerializationStrategy serializationStrategy) {
        Path directory = Paths.get(dir);
        Objects.requireNonNull(dir, "directory must not be null");
        this.serializationStrategy = serializationStrategy;
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not a directory or is not writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        getFilesList().forEach(this::doDelete);
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
            doUpdate(path, resume);
        } catch (IOException e) {
            throw new StorageException("IO Error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path is not deleted", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serializationStrategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path is not found", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            serializationStrategy.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path is not written", path.getFileName().toString(), e);
        }
    }

    @Override
    public int size() {
        return (int) getFilesList().count();
    }

    @Override
    protected boolean isFound(Path path) {
        return Files.exists(path);
    }

    @Override
    protected List<Resume> getResumeList() {
        return getFilesList().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    protected Path findSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    private Stream<Path> getFilesList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory can't be read");
        }
    }

}
