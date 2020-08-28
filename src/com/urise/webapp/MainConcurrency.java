package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ListStorage;
import com.urise.webapp.storage.Storage;

public class MainConcurrency {

    private static final Storage STORAGE_1 = new ListStorage();
    private static final Storage STORAGE_2 = new ListStorage();

    public static void main(String[] args) {
        deadlock();
    }

    private static void deadlock() {
        lock(STORAGE_2, STORAGE_1);
        lock(STORAGE_1, STORAGE_2);
    }

    private static void lock(Storage storage1, Storage storage2) {
        new Thread(() -> {
            synchronized (storage2) {
                System.out.println(Thread.currentThread().getName() + " has locked resource: " + storage1.hashCode());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (storage1) {
                    System.out.println(Thread.currentThread().getName() + " has locked resource: " + storage2.hashCode());
                    storage1.save(new Resume());
                }
            }
        }).start();
    }
}
