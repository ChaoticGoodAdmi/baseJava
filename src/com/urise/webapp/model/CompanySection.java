package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class CompanySection implements Section {

    private final List<Experience> list;

    public CompanySection(List<Experience> list) {
        this.list = list;
    }

    public List<Experience> getList() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Experience item : list) {
            sb.append(item.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySection that = (CompanySection) o;
        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
