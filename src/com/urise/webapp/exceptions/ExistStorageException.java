package com.urise.webapp.exceptions;

public class ExistStorageException extends StorageException {

    private static final long serialVersionUID = 1L;

    public ExistStorageException(String uuid) {
        super(uuid, "Resume " + uuid + " already exists in a storage");
    }
}
