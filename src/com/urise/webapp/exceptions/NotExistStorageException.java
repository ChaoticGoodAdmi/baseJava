package com.urise.webapp.exceptions;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException(String uuid) {
        super(uuid, "Резюме " + uuid + " не найдено в хранилище");
    }
}
