package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final Company EMPTY = new Company(Link.EMPTY, Collections.singletonList(Position.EMPTY));

    private Link homePage;
    private List<Position> positions;

    public Company() {
    }

    public Company(Link homePage, List<Position> positions) {
        Objects.requireNonNull(positions, "positions list must not be null");
        this.homePage = homePage;
        this.positions = positions;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(homePage).append("\n");
        Collections.sort(positions);
        for (Position position : positions) {
            sb.append(position.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company that = (Company) o;
        return homePage.equals(that.homePage) &&
                positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, positions);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Comparable<Position>, Serializable {
        private static final long serialVersionUID = 1L;
        public static final Position EMPTY = new Position();

        private String title;
        private String description;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate endDate;

        public Position() {
        }

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
