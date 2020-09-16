package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.Validator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uuid = req.getParameter("uuid");
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("resumes", storage.getAllSorted());
            req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
            return;
        }
        processAction(req, resp, uuid, action);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String uuid = req.getParameter("uuid");
        String fullName = req.getParameter("fullName");
        List<String> validatingProblems = new ArrayList<>();
        if (fullName == null || fullName.trim().length() == 0) {
            validatingProblems.add("Name must not be empty");
        }
        Resume r;
        boolean isCreating = uuid.equals("");
        if (!isCreating) {
            r = storage.get(uuid);
            fillResume(req, r, fullName);
        } else {
            r = new Resume(fullName);
            fillResume(req, r, fullName);
        }
        validateResume(r, validatingProblems);
        if (validatingProblems.size() > 0) {
            req.setAttribute("problems", validatingProblems);
            req.setAttribute("resume", r);
            req.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(req, resp);
        } else {
            if (!isCreating) {
                storage.update(r);
            } else {
                storage.save(r);
            }
            resp.sendRedirect("resume");
        }
    }

    private void processAction(HttpServletRequest req, HttpServletResponse resp, String uuid, String action)
            throws IOException, ServletException {
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                resp.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                if (!uuid.equals("")) {
                    r = storage.get(uuid);
                    insertEmptyCompany(r, SectionType.EXPERIENCE);
                    insertEmptyCompany(r, SectionType.EDUCATION);
                } else {
                    r = getEmptyResume();
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        req.setAttribute("resume", r);
        req.setAttribute("problems", new ArrayList<String>());
        req.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(req, resp);
    }

    private Resume getEmptyResume() {
        Resume r = new Resume();
        for (ContactType contactType : ContactType.values()) {
            r.setContact(contactType, "");
        }
        for (SectionType sectionType : SectionType.values()) {
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    r.setSection(sectionType, new TextSection(""));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    r.setSection(sectionType, new ListSection(Collections.singletonList("")));
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    r.setSection(sectionType, new CompanySection(Collections.singletonList(
                            getEmptyCompany())));
                    break;
            }
        }
        return r;
    }

    private void fillResume(HttpServletRequest req, Resume r, String fullName) {
        r.setFullName(fullName);
        fillContacts(req, r);
        fillSections(req, r);
    }

    private void fillContacts(HttpServletRequest req, Resume r) {
        for (ContactType type : ContactType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
    }

    private void fillSections(HttpServletRequest req, Resume r) {
        for (SectionType type : SectionType.values()) {
            String value = req.getParameter(type.name());
            String[] values = req.getParameterValues(type.name() + ".companyName");
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    r.setSection(type, new TextSection(value));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    List<String> list = Stream.of(value.trim().split("\n"))
                            .filter(entity -> entity.trim().length() > 0)
                            .map(String::new)
                            .collect(Collectors.toList());
                    r.setSection(type, new ListSection(list));
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    List<Company> companies = new ArrayList<>();
                    if (values != null) {
                        String[] companyUrls = req.getParameterValues(type.name() + ".companyUrl");
                        for (int i = 0; i < values.length; i++) {
                            if (!values[i].equals("")) {
                                List<Company.Position> positions = getPositions(req, type, i);
                                companies.add(new Company(new Link(companyUrls[i], values[i]), positions));
                            }
                        }
                    }
                    r.setSection(type, new CompanySection(companies));
                    break;
            }
        }
    }

    private List<Company.Position> getPositions(HttpServletRequest req, SectionType type, int i) {
        List<Company.Position> positions = new ArrayList<>();
        String prefix = type.name() + "." + i + ".";
        String[] startMonths = req.getParameterValues(prefix + "startMonth");
        String[] startYears = req.getParameterValues(prefix + "startYear");
        String[] endMonths = req.getParameterValues(prefix + "endMonth");
        String[] endYears = req.getParameterValues(prefix + "endYear");
        String[] titles = req.getParameterValues(prefix + "title");
        String[] descriptions = req.getParameterValues(prefix + "description");
        for (int j = 0; j < titles.length; j++) {
            if (!titles[j].equals("")) {
                positions.add(endMonths[j].equals("") && endYears[j].equals("") ?
                        new Company.Position(titles[j], descriptions[j],
                                Integer.parseInt(startYears[j]), Month.of(Integer.parseInt(startMonths[j]))) :
                        new Company.Position(titles[j], descriptions[j],
                                Integer.parseInt(startYears[j]), Month.of(Integer.parseInt(startMonths[j])),
                                Integer.parseInt(endYears[j]), Month.of(Integer.parseInt(endMonths[j]))));
            }
        }
        return positions;
    }

    private void validateResume(Resume resume, List<String> validatingProblems) {
        String phoneNumber = resume.getContact(ContactType.PHONE_NUMBER);
        validate(phoneNumber, Validator.validatePhoneNumber(phoneNumber),
                s -> validatingProblems.add("Phone number must be like this: 79009991122"));

        String email = resume.getContact(ContactType.EMAIL);
        validate(email, Validator.validateEmail(email),
                s -> validatingProblems.add("E-mail address is incorrect"));

        String linkedInProfile = resume.getContact(ContactType.LINKED_IN);
        validate(linkedInProfile, Validator.validateUrl(linkedInProfile, "linkedin.com"),
                s -> validatingProblems.add("Link to a LinkedIn profile is incorrect"));

        String gitProfile = resume.getContact(ContactType.GITHUB);
        validate(gitProfile, Validator.validateUrl(gitProfile, "github.com"),
                s -> validatingProblems.add("Link to a Github profile is incorrect"));

        String stackProfile = resume.getContact(ContactType.STACKOVERFLOW);
        validate(stackProfile, Validator.validateUrl(stackProfile, "stackoverflow.com"),
                s -> validatingProblems.add("Link to a StackOverflow profile is incorrect"));

        String homePage = resume.getContact(ContactType.HOME_PAGE);
        validate(homePage, Validator.validateUrl(homePage),
                s -> validatingProblems.add("Home page must be a valid URL"));

    }

    private void validate(String value, Boolean condition, Consumer<String> consumer) {
        if (!value.equals("") && (value.trim().length() == 0 || !condition)) {
            consumer.accept(value);
        }
    }

    private Company getEmptyCompany() {
        return new Company(new Link("", ""),
                Collections.singletonList(
                        new Company.Position()));
    }

    private void insertEmptyCompany(Resume r, SectionType education) {
        ((CompanySection) r.getSection(education)).getList().add(getEmptyCompany());
    }
}
