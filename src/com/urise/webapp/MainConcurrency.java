package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ListStorage;
import com.urise.webapp.storage.Storage;

public class MainConcurrency {

    public static final Storage STORAGE_1 = new ListStorage();
    public static final Storage STORAGE_2 = new ListStorage();

    public static void main(String[] args) {
        deadlock();
    }

    private static void deadlock() {
        new Thread(() -> {
            synchronized (STORAGE_2) {
                System.out.println(Thread.currentThread().getName() + " has locked STORAGE_2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (STORAGE_1) {
                    System.out.println(Thread.currentThread().getName() + " has locked STORAGE_1");
                    for (int i = 0; i < 10; i++) {
                        STORAGE_1.save(new Resume(Integer.toString(i)));
                    }
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (STORAGE_1) {
                System.out.println(Thread.currentThread().getName() + " has locked STORAGE_1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (STORAGE_2) {
                    System.out.println(Thread.currentThread().getName() + " has locked STORAGE_2");
                    for (int i = 0; i < 10; i++) {
                        STORAGE_2.save(new Resume(Integer.toString(i)));
                    }
                }
            }
        }).start();
    }
}
