package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exceptions.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");
        printHtmlHeader(writer);
        String uuid = req.getParameter("uuid");
        if (uuid != null) {
            try {
                Resume resume = storage.get(uuid);
                writer.write("<a href=\"resume\"><<<</a>");
                printResume(writer, resume);
            } catch (NotExistStorageException e) {
                writer.write("There is no such resume in a database");
            }
        } else {
            printAllResumes(writer);
        }
        printHtmlCloser(writer);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    }

    private void printResume(PrintWriter writer, Resume resume) {
        writer.write("<h3>UUID:</h3>");
        writer.write(resume.getUuid());
        writer.write("<h3>Name:</h3>");
        writer.write(resume.getFullName());
        printContacts(writer, resume);
        printSections(writer, resume);
    }

    private void printAllResumes(PrintWriter writer) {
        writer.write("<table><tr><th>UUID</th><th>Name</th>");
        List<Resume> resumes = storage.getAllSorted();
        for (Resume r : resumes) {
            String id = r.getUuid();
            printTableRow(writer,
                    "<a href=\"\\resumes\\resume?uuid=" + id + "\">" + r.getUuid() + "</a>",
                    r.getFullName());
        }
    }

    private void printContacts(PrintWriter writer, Resume resume) {
        writer.write("<h3>Contacts</h3>");
        for (Map.Entry<ContactType, String> contactEntry : resume.getContacts().entrySet()) {
            writer.write("<div>" + contactEntry.getValue() + "</div>");
        }
    }

    private void printSections(PrintWriter writer, Resume resume) {
        writer.write("<h3>Contacts</h3>");
        for (Map.Entry<SectionType, Section> sectionEntry : resume.getSections().entrySet()) {
            writer.write("<h3>" + sectionEntry.getKey() + "</h3>");
            writer.write(String.valueOf(sectionEntry.getValue()));
        }
    }

    private void printTableRow(PrintWriter writer, String... text) {
        writer.write("<tr>");
        for (String element : text) {
            writer.write("<td>");
            writer.write(element);
            writer.write("</td>");
        }
        writer.write("</tr>");
    }

    private void printHtmlHeader(PrintWriter writer) {
        writer.write("<!DOCTYPE html>\n" +
                "            <html>\n" +
                "                <head>\n" +
                "                    <meta charset=\"UTF-8\">" +
                "                    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "                    <title>Resumes</title>\n" +
                "                </head>" +
                "                <body>");
    }

    private void printHtmlCloser(PrintWriter writer) {
        writer.write("</body>" +
                "     </html>");
    }
}
