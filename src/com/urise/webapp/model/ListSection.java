package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection<T> implements Section {

    private List<T> list = new ArrayList<>();

    public ListSection(List<T> list) {
        this.list = list;
    }

    public ListSection<String> listStringSection(List<String> list) {
        return new ListSection<>(list);
    }

    public ListSection<Business> listBusinessSection(List<Business> list) {
        return new ListSection<>(list);
    }
}
