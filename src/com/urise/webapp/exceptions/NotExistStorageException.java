package com.urise.webapp.exceptions;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException(String uuid) {
        super(uuid, "Resume " + uuid + " does not exist in a storage");
    }
}
