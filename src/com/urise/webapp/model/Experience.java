package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Experience {

    private final Link homePage;
    private List<Position> positions;


    public Experience(Link homePage, List<Position> positions) {
        Objects.requireNonNull(positions, "positions list must not be null");
        this.homePage = homePage;
        this.positions = positions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(homePage).append("\n");
        Collections.sort(positions);
        for(Position position : positions) {
            sb.append(position.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return homePage.equals(that.homePage) &&
                positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, positions);
    }

    public static class Position implements Comparable<Position>{

        private final String title;
        private final String description;
        private final LocalDate startDate;
        private final LocalDate endDate;

        public Position(String title, String description, int startYear, Month startMonth, int endYear, Month endMonth) {
            this.title = title;
            this.description = description;
            this.startDate = DateUtil.of(startYear, startMonth);
            this.endDate = DateUtil.of(endYear, endMonth);
        }

        public Position(String title, String description, int startYear, Month startMonth) {
            this(title, description, startYear, startMonth, LocalDate.now().getYear(), LocalDate.now().getMonth());
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(startDate.getYear()).append("/").append(startDate.getMonth());
            sb.append(" - ");
            sb.append(endDate.getYear()).append("/").append(endDate.getMonth());
            sb.append("\n");
            sb.append(title).append("\n");
            if (description != null)
                sb.append(description).append("\n");
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return title.equals(position.title) &&
                    Objects.equals(description, position.description) &&
                    startDate.equals(position.startDate) &&
                    Objects.equals(endDate, position.endDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description, startDate, endDate);
        }

        @Override
        public int compareTo(Position o) {
            return this.startDate.compareTo(o.getStartDate());
        }
    }
}
