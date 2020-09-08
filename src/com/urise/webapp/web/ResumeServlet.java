package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        req.setAttribute("resumes", storage.getAllSorted());
        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) {
    }

}
