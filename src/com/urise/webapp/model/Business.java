package com.urise.webapp.model;

import java.time.LocalDate;

public class Business {

    private final String companyName;
    private final Link homePage;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String title;
    private final String description;

    public Business(String companyName, Link homePage, LocalDate startDate, String title, String description) {
        this(companyName, homePage, startDate, null, title, description);
    }

    public Business(String companyName, Link homePage, LocalDate startDate, LocalDate endDate, String title, String description) {
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
        sb.append(title).append("\n");
        if (description != null)
            sb.append(description).append("\n");
        return sb.toString();
    }
}
