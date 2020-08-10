package com.urise.webapp.exceptions;

public class NotExistStorageException extends StorageException {

    private static final long serialVersionUID = 1L;

    public NotExistStorageException(String uuid) {
        super(uuid, "Resume " + uuid + " does not exist in a storage");
    }
}
