package com.urise.webapp.model;

import java.util.List;

public class ListSection<T> implements Section {

    private final List<T> list;

    private ListSection(List<T> list) {
        this.list = list;
    }

    public static ListSection<String> listStringSection(List<String> list) {
        return new ListSection<>(list);
    }

    public static ListSection<Business> listBusinessSection(List<Business> list) {
        return new ListSection<>(list);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(T item : list) {
            sb.append(item.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
