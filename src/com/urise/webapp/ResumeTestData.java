package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ResumeTestData {

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

    public static Resume createTestResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            resume.setContact(entry.getKey(), entry.getValue());
        }
        resume.setSection(SectionType.PERSONAL,
                new TextSection("text of personal qualities"));
        resume.setSection(SectionType.OBJECTIVE,
                new TextSection("Senior Java Developer"));
        resume.setSection(SectionType.ACHIEVEMENT,
                new ListSection(
                        new ArrayList<>(Arrays.asList(
                                "Achievement_1",
                                "Achievement_2",
                                "Achievement_3"
                        ))
                )
        );
        resume.setSection(SectionType.QUALIFICATIONS,
                new ListSection(
                        new ArrayList<>(Arrays.asList(
                                "Qualification_1",
                                "Qualification_2",
                                "Qualification_3"
                        ))
                )
        );
        resume.setSection(SectionType.EXPERIENCE,
                new CompanySection(
                        new ArrayList<>(Arrays.asList(
                                new Company(
                                        new Link("www.comp1.com", "Company_1"),
                                        new ArrayList<>(Arrays.asList(
                                                new Company.Position(
                                                        "Job_title_2",
                                                        "Job_description_2",
                                                        2010,
                                                        Month.APRIL
                                                ),
                                                new Company.Position(
                                                        "Job_title_1",
                                                        "Job_description_1",
                                                        2000,
                                                        Month.AUGUST,
                                                        2010,
                                                        Month.APRIL
                                                )
                                        ))
                                ),
                                new Company(
                                        new Link("www.comp2.com", "Company_2"),
                                        new ArrayList<>(Arrays.asList(
                                                new Company.Position(
                                                        "Job_title",
                                                        "Job_description",
                                                        1990,
                                                        Month.JANUARY,
                                                        2000,
                                                        Month.AUGUST
                                                )
                                        ))
                                ))
                        )
                )
        );
        resume.setSection(SectionType.EDUCATION,
                new CompanySection(
                        new ArrayList<>(Arrays.asList(
                                new Company(
                                        new Link("www.uni1.com", "University_1"),
                                        new ArrayList<>(Arrays.asList(
                                                new Company.Position(
                                                        "Course_title_1",
                                                        "",
                                                        1980,
                                                        Month.SEPTEMBER,
                                                        1985,
                                                        Month.JULY
                                                ),
                                                new Company.Position(
                                                        "Course_title_2",
                                                        "",
                                                        1985,
                                                        Month.SEPTEMBER,
                                                        1990,
                                                        Month.JULY
                                                )
                                        ))
                                ),
                                new Company(
                                        new Link("www.uni2.com", "University_2"),
                                        new ArrayList<>(Arrays.asList(
                                                new Company.Position(
                                                        "Course_title",
                                                        "",
                                                        1990,
                                                        Month.JULY,
                                                        1990,
                                                        Month.AUGUST
                                                )
                                        ))
                                )
                        ))
                )
        );
        return resume;
    }
}
