<%@ page import="com.urise.webapp.model.CompanySection" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%--suppress XmlPathReference --%>
<%@ page contentType="text/html;charset=UTF-8" %>
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
<jsp:include page="fragments/footer.jsp"/>
<section>
    <jsp:useBean id="problems" scope="request" type="java.util.List"/>
    <c:forEach var="problem" items="<%=problems%>">
        <span style="color: red; ">${problem}</span><br>
    </c:forEach>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input name="uuid" type="hidden" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
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
        <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
        <c:set var="sectionType" value="${sectionEntry.key}"/>
        <jsp:useBean id="sectionType" type="com.urise.webapp.model.SectionType"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <jsp:useBean id="section" type="com.urise.webapp.model.Section"/>
        <dl>
            <dt><%=sectionType.getTitle()%>
            </dt>
            <dd><c:choose>
                <c:when test="${sectionType == 'PERSONAL'}">
                    <textarea name='${sectionType}' cols=50 rows=10><%=section%></textarea>
                </c:when>
                <c:when test="${sectionType == 'OBJECTIVE'}">
                    <input type="text" name=${sectionType.name()} size="45" value="${resume.getSection(sectionType)}">
                </c:when>
                <c:when test="${sectionType == 'ACHIEVEMENT' || sectionType == 'QUALIFICATIONS'}">
                    <textarea name='${sectionType}' cols=50
                              rows=10><%=String.join("\n", ((ListSection) section).getList())%>
                    </textarea>
                </c:when>
                <c:when test="${sectionType == 'EXPERIENCE' || sectionType == 'EDUCATION'}">
                    <c:forEach var="company" items="<%=((CompanySection) section).getList()%>">
                        <jsp:useBean id="company" type="com.urise.webapp.model.Company"/>
                        <input type="text" name="companyName" size="45"
                               placeholder="Company name" value="${company.homePage.name}">
                        <input type="text" name="companyUrl" size="45"
                               placeholder="Company web-page" value="${company.homePage.url}">
                        <c:forEach var="position" items="<%=company.getPositions()%>">
                            <jsp:useBean id="position" type="com.urise.webapp.model.Company.Position"/>
                            <table cellpadding="10px">
                            <tr>
                                <td style="vertical-align: top">Date of working:
                                    <jsp:useBean id="date" class="java.util.Date"/>
                                    <fmt:formatDate value="${date}" pattern="yyyy" var="currentYear"/>
                                    <input type="number" name="startMonth"
                                           max="12" min="1" maxlength="2" style="width: 40px"
                                           value="${position.startDate.month.value}">/
                                    <input type="number" name="startYear"
                                           max="${currentYear}" min="1900" maxlength="4" style="width: 60px"
                                           value="${position.startDate.year}">
                                    -
                                    <input type="number" name="endMonth"
                                           max="12" min="1" maxlength="2" style="width: 40px"
                                           value="${position.endDate.month.value}">/
                                    <input type="number" name="endYear"
                                           max="${currentYear}" min="1900" maxlength="4" style="width: 60px"
                                           value="${position.endDate.year}">
                                </td>
                                <td>
                                    <input type="text" size="45" name="jobTitle" placeholder="Job title"
                                           value="${position.title}"><br>
                                    <input type="text" size="45" name="jobDescription" placeholder="jobDescription"
                                           value="${position.description}">
                                </td>
                            </tr>
                        </c:forEach>
                        </table>
                    </c:forEach>
                    <input type="text" name="newCompanyName" size="45"
                           placeholder="Company name" value="${company.homePage.name}">
                    <input type="text" name="newCompanyUrl" size="45"
                           placeholder="Company web-page" value="${company.homePage.url}">
                    <br>
                    <p>Add new one here:</p>
                    <table>
                        <tr>
                            <td style="vertical-align: top">Date of working:
                                <input type="number" name="newStartMonth"
                                       max="12" min="1" maxlength="2" style="width: 40px"
                                       value="">/
                                <input type="number" name="newStartYear"
                                       max="${currentYear}" min="1990" maxlength="4" style="width: 60px"
                                       value="">
                                -
                                <input type="number" name="newEndMonth"
                                       max="12" min="1" maxlength="2" style="width: 40px"
                                       value="">/
                                <input type="number" name="newEndYear"
                                       max="${currentYear}" min="1900" maxlength="4" style="width: 60px"
                                       value="">
                            </td>
                            <td>
                                <input type="text" size="45" name="newJobTitle" placeholder="Job title"
                                       value=""><br>
                                <input type="text" size="45" name="newJobDescription" placeholder="jobDescription"
                                       value="">
                            </td>
                        </tr>
                    </table>
                </c:when>
            </c:choose></dd>
            </c:forEach>
        </dl>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>