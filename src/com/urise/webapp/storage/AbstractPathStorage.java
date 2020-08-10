package com.urise.webapp.storage;

import com.urise.webapp.exceptions.StorageException;
import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {

    private Path directory;

    protected AbstractPathStorage(String dir) {
        Path directory = Paths.get(dir);
        Objects.requireNonNull(dir, "directory must not be null");
        if(!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + "is not a directory or is not writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error");
        }
    }

    @Override
    protected void doSave(Resume resume, Path Path) {
/*        try {
            Path.createNewPath();
            doWrite(resume, new BufferedOutputStream(new PathOutputStream(Path)));
        } catch (IOException e) {
            throw new StorageException("IO Error", Path.getAbsolutePath(), e);
        }*/
    }

    @Override
    protected void doDelete(Path Path) {
/*        if (!Path.delete())
            throw new StorageException("Path is not deleted");*/
    }

    @Override
    protected Resume doGet(Path Path) {
/*        try {
            return doRead(new BufferedInputStream(new PathInputStream(Path)));
        } catch (IOException e) {
            throw new StorageException("Path is not found", Path.getAbsolutePath(), e);
        }*/
return null;
    }

    @Override
    protected void doUpdate(Path Path, Resume resume) {
/*        try {
            doWrite(resume, new BufferedOutputStream(new PathOutputStream(Path)));
        } catch (IOException e) {
            throw new StorageException("Path is not written", Path.getAbsolutePath(), e);
        }*/
    }

    @Override
    public int size() {
/*        String[] resumePaths = directory.list();
        if (resumePaths == null)
            throw new StorageException("Directory can't be read");
        else
            return resumePaths.length;*/
return 0;
    }

    @Override
    protected boolean isFound(Path Path) {
        /*return Path.exists();*/
        return false;
    }

    @Override
    protected List<Resume> getResumeList() {
/*        List<Resume> resumeList = new ArrayList<>();
        Path[] resumePaths = directory.listPaths();
        if (resumePaths == null)
            throw new StorageException("Directory can't be read");
        else {
            for (Path resumePath : resumePaths) {
                resumeList.add(doGet(resumePath));
            }
        }
        return resumeList;*/
return null;
    }

    @Override
    protected Path findSearchKey(String uuid) {
        /*return new Path(directory, uuid);*/
        return null;
    }

    protected abstract void doWrite(Resume resume, OutputStream os) throws IOException;

    protected abstract Resume doRead(InputStream is) throws IOException;

}
