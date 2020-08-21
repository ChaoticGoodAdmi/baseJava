package com.urise.webapp.storage;

import com.urise.webapp.storage.serialization.ObjectStreamSerialization;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerialization()));
    }
}
