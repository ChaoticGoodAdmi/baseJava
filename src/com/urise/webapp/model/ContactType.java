package com.urise.webapp.model;

public enum ContactType {
    PHONE_NUMBER("Тел."),
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return getAsLink(value, "<a href='skype:", value);
        }
    },
    EMAIL("Почта") {
        @Override
        public String toHtml0(String value) {
            return getAsLink(value, "<a href='mailTo:", value);
        }
    },
    LINKED_IN("Профиль LinkedIn") {
        @Override
        public String toHtml0(String value) {
            return getAsLink(value, "<a href='", getTitle());
        }
    },
    GITHUB("Профиль GitHub") {
        @Override
        public String toHtml0(String value) {
            return getAsLink(value, "<a href='", getTitle());
        }
    },
    STACKOVERFLOW("Профиль Stackoverflow") {
        @Override
        public String toHtml0(String value) {
            return getAsLink(value, "<a href='", getTitle());
        }
    },
    HOME_PAGE("Домашняя страница") {
        @Override
        public String toHtml0(String value) {
            return getTitle() + ": <a href='//" + value + "'>" + value + "</a>";
        }
    };

    private static String getAsLink(String value, String openTag, String value2) {
        return openTag + value + "'>" + value2 + "</a>";
    }

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
