<%@ page import="com.urise.webapp.model.ContactType" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/list_style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <br>
    <dl>
        <a class="button" href="resume?uuid=&action=edit">
            <img src="img/add.png" alt="Add new resume"> Add new resume
        </a>
    </dl>
    <br>
    <table class="resumeList">
        <thead>
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
        <c:forEach items="${resumes}" var="resume">
        <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
        <tr>
            <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
            <td><%=ContactType.EMAIL.toHtml(resume.getContact(ContactType.EMAIL))%>
            </td>
            <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png" alt="Delete"></a></td>
            <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png" alt="Edit"></a></td>
        </tr>
        </tbody>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>