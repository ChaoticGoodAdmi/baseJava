package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init (ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uuid = req.getParameter("uuid");
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("resumes", storage.getAllSorted());
            req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                resp.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        req.setAttribute("resume", r);
        req.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(req, resp);
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String uuid = req.getParameter("uuid");
        String fullName = req.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        storage.update(r);
        resp.sendRedirect("resume");
    }

}
