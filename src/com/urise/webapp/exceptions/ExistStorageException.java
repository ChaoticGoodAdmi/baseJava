package com.urise.webapp.exceptions;

public class ExistStorageException extends StorageException{

    public ExistStorageException(String uuid) {
        super(uuid, "Resume " + uuid + " already exists in a storage");
    }
}
