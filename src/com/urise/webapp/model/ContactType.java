package com.urise.webapp.model;

public enum ContactType {
    PHONE_NUMBER("Тел."),
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    EMAIL("Почта") {
        @Override
        public String toHtml0(String value) {
            return "<a href='mailTo:" + value + "'>" + value + "</a>";
        }
    },
    LINKED_IN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOME_PAGE("Домашняя страница");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toHtml(String value) {
        return value != null ? toHtml0(value) : "";
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }
}
