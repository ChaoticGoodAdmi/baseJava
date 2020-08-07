package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Experience {

    private final String companyName;
    private final Link homePage;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String title;
    private final String description;

    public Experience(String companyName, Link homePage, LocalDate startDate, String title, String description) {
        this(companyName, homePage, startDate, null, title, description);
    }

    public Experience(String companyName, Link homePage, LocalDate startDate, LocalDate endDate, String title, String description) {
        this.companyName = companyName;
        this.homePage = homePage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(companyName).append(" (").append(homePage).append(") \n");
        sb.append("Period: ").append(startDate).append(" - ");
        if (endDate != null)
            sb.append(endDate);
        else
            sb.append("now");
        sb.append("\n");
        sb.append(title);
        if (description != null)
            sb.append("\n").append(description);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return companyName.equals(that.companyName) &&
                homePage.equals(that.homePage) &&
                startDate.equals(that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                title.equals(that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, homePage, startDate, endDate, title, description);
    }
}
