package com.urise.webapp.model;

import java.time.LocalDate;

public class Business {

    private String companyName;
    private Link homePage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String text;

    public Business(String companyName, Link homePage, LocalDate startDate, LocalDate endDate, String title, String text) {
        this.companyName = companyName;
        this.homePage = homePage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.text = text;
    }
}
