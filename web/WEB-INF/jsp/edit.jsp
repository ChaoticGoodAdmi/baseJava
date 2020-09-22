<%@ page import="com.urise.webapp.model.CompanySection" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%--suppress XmlPathReference --%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<br>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <jsp:useBean id="problems" scope="request" type="java.util.List"/>
        <c:forEach var="problem" items="<%=problems%>">
            <span style="color: red; ">${problem}</span><br>
        </c:forEach>
        <input name="uuid" type="hidden" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" required size="50" value="${resume.fullName}"></dd>
        </dl>
        <hr>
        <h3>Контакты:</h3>
        <hr>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name=${type.name()} size="30" value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <hr>
        <c:forEach var="sectionType" items="<%=SectionType.values()%>">
        <c:set var="section" value="${resume.getSection(sectionType)}"/>
        <jsp:useBean id="section" type="com.urise.webapp.model.Section"/>
        <dl>
            <dt>${sectionType.title}
            </dt>
            <dd><c:choose>
                <c:when test="${sectionType == 'PERSONAL'}">
                    <textarea name='${sectionType}' cols=50 rows=10><%=section%></textarea>
                </c:when>
                <c:when test="${sectionType == 'OBJECTIVE'}">
                    <input type="text" name=${sectionType.name()} size="45" value="${section}">
                </c:when>
                <c:when test="${sectionType == 'ACHIEVEMENT' || sectionType == 'QUALIFICATIONS'}">
                    <textarea name='${sectionType}' cols=50
                              rows=10><%=String.join("\n", ((ListSection) section).getList())%>
                    </textarea>
                </c:when>
                <c:when test="${sectionType == 'EXPERIENCE' || sectionType == 'EDUCATION'}">
                    <c:forEach var="company" items="<%=((CompanySection) section).getList()%>" varStatus="counter">
                        <jsp:useBean id="company" type="com.urise.webapp.model.Company"/>
                        <input type="text" name='${sectionType}.companyName' size="45"
                               placeholder="Company name" value="${company.homePage.name}">
                        <input type="text" name='${sectionType}.companyUrl' size="45"
                               placeholder="Company web-page" value="${company.homePage.url}">
                        <c:forEach var="position" items="<%=company.getPositions()%>">
                            <jsp:useBean id="position" type="com.urise.webapp.model.Company.Position"/>
                            <table>
                            <tr>
                                <td style="vertical-align: top">Date of working:
                                    <jsp:useBean id="date" class="java.util.Date"/>
                                    <fmt:formatDate value="${date}" pattern="yyyy" var="currentYear"/>
                                    <input type="number" name='${sectionType}.${counter.index}.startMonth'
                                           max="12" min="1" maxlength="2" style="width: 40px"
                                           value="${position.startDate.month.value}">/
                                    <input type="number" name='${sectionType}.${counter.index}.startYear'
                                           max="${currentYear}" min="1900" maxlength="4" style="width: 60px"
                                           value="${position.startDate.year}">
                                    -
                                    <input type="number" name='${sectionType}.${counter.index}.endMonth'
                                           max="12" min="1" maxlength="2" style="width: 40px"
                                           value="${position.endDate.month.value}">/
                                    <input type="number" name='${sectionType}.${counter.index}.endYear'
                                           max="${currentYear}" min="1900" maxlength="4" style="width: 60px"
                                           value="${position.endDate.year}">
                                </td>
                                <td>
                                    <input type="text" size="45" name='${sectionType}.${counter.index}.title'
                                           placeholder="Title" value="${position.title}"><br>
                                    <input type="text" size="45" name='${sectionType}.${counter.index}.description'
                                           placeholder="Description" value="${position.description}">
                                </td>
                            </tr>
                        </c:forEach>
                        </table>
                    </c:forEach>
                </c:when>
            </c:choose></dd>
            </c:forEach>
        </dl>
        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>