package com.urise.webapp.storage;

import com.urise.webapp.storage.serialization.DataStreamSerialization;

public class DataStreamStorageTest extends AbstractStorageTest {

    public DataStreamStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerialization()));
    }
}
