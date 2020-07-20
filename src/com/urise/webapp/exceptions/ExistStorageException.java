package com.urise.webapp.exceptions;

public class ExistStorageException extends StorageException{

    public ExistStorageException(String uuid) {
        super(uuid, "Резюме " + uuid + " уже добавлено в хранилище");
    }
}
