package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection<T> implements Section {

    private final List<T> list;

    private ListSection(List<T> list) {
        this.list = list;
    }

    public static ListSection<String> listStringSection(List<String> list) {
        return new ListSection<>(list);
    }

    public static ListSection<Experience> listBusinessSection(List<Experience> list) {
        return new ListSection<>(list);
    }

    public List<T> getList() {
        return list;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection<?> that = (ListSection<?>) o;
        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
