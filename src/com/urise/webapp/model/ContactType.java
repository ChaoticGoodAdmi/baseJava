package com.urise.webapp.model;

public enum ContactType {
    PHONE_NUMBER("Тел."),
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return "<img src='img/skype.png'> <a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    EMAIL("Почта") {
        @Override
        public String toHtml0(String value) {
            return "<img src='img/email.png'> <a href='mailTo:" + value + "'>" + value + "</a>";
        }
    },
    LINKED_IN("Профиль LinkedIn") {
        @Override
        public String toHtml0(String value) {
            return "<img src='img/lin.png'> <a href='" + value + "'>" + getTitle() + "</a>";
        }
    },
    GITHUB("Профиль GitHub") {
        @Override
        public String toHtml0(String value) {
            return "<img src='img/gh.png'> <a href='" + value + "'>" + getTitle() + "</a>";
        }
    },
    STACKOVERFLOW("Профиль Stackoverflow") {
        @Override
        public String toHtml0(String value) {
            return "<img src='img/so.png'> <a href='" + value + "'>" + getTitle() + "</a>";
        }
    },
    HOME_PAGE("Домашняя страница") {
        @Override
        public String toHtml0(String value) {
            return getTitle() + ": <a href='//" + value + "'>" + value + "</a>";
        }
    };

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
