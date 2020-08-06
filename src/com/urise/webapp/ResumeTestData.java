package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ResumeTestData {

    private final static Resume resume = new Resume("John Doe");
    private final static Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);

    static {
        contacts.put(ContactType.PHONE_NUMBER, "+7 (999) 999 99 99");
        contacts.put(ContactType.SKYPE, "skype.profile");
        contacts.put(ContactType.EMAIL, "email@adress.com");
        contacts.put(ContactType.LINKED_IN, "https://www.linkedin.com/in/profile");
        contacts.put(ContactType.GITHUB, "https://github.com/profile");
        contacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/0");
        contacts.put(ContactType.HOME_PAGE, "homepage.com");
    }

    public static void main(String[] args) {
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            resume.setContact(entry.getKey(), entry.getValue());
        }
        resume.setSection(SectionType.PERSONAL,
                new TextSection("text of personal qualities"));
        resume.setSection(SectionType.OBJECTIVE,
                new TextSection("Senior Java Developer"));
        resume.setSection(SectionType.ACHIEVEMENT,
                ListSection.listStringSection(
                        new ArrayList<>(Arrays.asList(
                                "Achievement_1",
                                "Achievement_2",
                                "Achievement_3"
                        ))
                )
        );
        resume.setSection(SectionType.QUALIFICATIONS,
                ListSection.listStringSection(
                        new ArrayList<>(Arrays.asList(
                                "Qualification_1",
                                "Qualification_2",
                                "Qualification_3"
                        ))
                )
        );
        resume.setSection(SectionType.EXPERIENCE,
                ListSection.listBusinessSection(
                        new ArrayList<>(Arrays.asList(
                                new Business(
                                        "Company_1",
                                        new Link("www.company1.com", "Company_1"),
                                        LocalDate.of(2013, 10, 1),
                                        "Position Title",
                                        "list of responsibilities"
                                ),
                                new Business(
                                        "Company_2",
                                        new Link("www.company2.com", "Company_1"),
                                        LocalDate.of(2012, 4, 1),
                                        LocalDate.of(2014, 10, 1),
                                        "Position Title",
                                        "list of responsibilities"
                                )
                        ))
                )
        );
        resume.setSection(SectionType.EDUCATION,
                ListSection.listBusinessSection(
                        new ArrayList<>(Arrays.asList(
                                new Business(
                                        "University_1",
                                        new Link("www.uni1.com", "University_1"),
                                        LocalDate.of(2013, 3, 1),
                                        LocalDate.of(2013, 5, 1),
                                        "Course_title",
                                        null
                                ),
                                new Business(
                                        "University_2",
                                        new Link("www.uni2.com", "University_2"),
                                        LocalDate.of(2012, 4, 1),
                                        LocalDate.of(2014, 10, 1),
                                        "Course_title",
                                        null
                                )
                        ))
                )
        );

        printResume(resume);
    }

    private static void printResume(Resume r) {
        printContacts(r);
        printSections(r);
    }

    private static void printContacts(Resume r) {
        System.out.println("Контакты: ");
        for (ContactType type : ContactType.values()) {
            System.out.println(type.getTitle() + ": " + r.getContact(type));
        }
        System.out.println();
    }

    private static void printSections(Resume r) {
        for (SectionType type : SectionType.values()) {
            System.out.println(type.getTitle());
            System.out.println(r.getSection(type));
        }
    }
}
