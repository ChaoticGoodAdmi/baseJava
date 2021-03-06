package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;
    public static final Resume EMPTY = new Resume("", "");

    static {
        for (ContactType contactType : ContactType.values()) {
            EMPTY.setContact(contactType, "");
        }
        for (SectionType sectionType : SectionType.values()) {
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    EMPTY.setSection(sectionType, TextSection.EMPTY);
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    EMPTY.setSection(sectionType, ListSection.EMPTY);
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    EMPTY.setSection(sectionType, CompanySection.EMPTY);
                    break;
            }
        }
    }

    private String uuid;
    private String fullName;
    private Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    public Resume() {
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "full name must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getContact(ContactType type) {
        String contact = getContacts().get(type);
        return contact != null ? getContacts().get(type) : "";
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public void setContact(ContactType type, String link) {
        contacts.put(type, link);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setContacts(Map<ContactType, String> contacts) {
        this.contacts = contacts;
    }

    public void setSection(SectionType type, Section section) {
        sections.put(type, section);
    }

    public void setSections(Map<SectionType, Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Resume{" +
                " uuid='" + uuid + '\'' +
                " fullName='" + fullName + '\'' +
                " contacts=" + contacts +
                " sections=" + sections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) &&
                fullName.equals(resume.fullName) &&
                contacts.equals(resume.contacts) &&
                sections.equals(resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public int compareTo(Resume resume) {
        int nameCompare = fullName.compareTo(resume.getFullName());
        return nameCompare == 0 ? uuid.compareTo(resume.uuid) : nameCompare;
    }
}
