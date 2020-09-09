<%--suppress XmlPathReference --%>
<%@ page import="com.urise.webapp.model.CompanySection" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="com.urise.webapp.model.TextSection" %>
<%--suppress XmlPathReference --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/footer.jsp"/>
<section>
    <dl><a href="resume">Back to resumes list</a></dl>
    <div>
        <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"
                                                                                          alt="Edit"></a></h2>
        <p>
            <c:forEach var="contactEntry" items="${resume.contacts}">
                <jsp:useBean id="contactEntry"
                             type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
            </c:forEach>
        </p>
    </div>
    <div>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
            <c:set var="sectionType" value="${sectionEntry.key}"/>
            <jsp:useBean id="sectionType" type="com.urise.webapp.model.SectionType"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.urise.webapp.model.Section"/>
            <h2>
                <%=sectionType.getTitle()%>
            </h2>
            <c:choose>
                <c:when test="${sectionType == 'PERSONAL' || sectionType == 'OBJECTIVE'}">
                    <%=((TextSection) section).getText()%>
                </c:when>
                <c:when test="${sectionType == 'ACHIEVEMENT' || sectionType == 'QUALIFICATIONS'}">
                    <ul>
                        <c:forEach var="element" items="<%=((ListSection) section).getList()%>">
                            <jsp:useBean id="element" type="java.lang.String"/>
                            <li>
                                <%=element%>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:when test="${sectionType == 'EXPERIENCE' || sectionType == 'EDUCATION'}">
                    <c:forEach var="company" items="<%=((CompanySection) section).getList()%>">
                        <jsp:useBean id="company" type="com.urise.webapp.model.Company"/>
                        <h3>
                            <c:choose>
                                <c:when test="${empty company.homePage.url}">
                                    ${company.homePage.name}
                                </c:when>
                                <c:otherwise>
                                    <a href="//${company.homePage.url}">${company.homePage.name}</a>
                                </c:otherwise>
                            </c:choose>
                        </h3>
                        <c:forEach var="position" items="<%=company.getPositions()%>">
                            <jsp:useBean id="position" type="com.urise.webapp.model.Company.Position"/>
                            <table cellpadding="10px">
                            <tr>
                                <td style="vertical-align: top">
                                    <%=position.getStartDate().getMonth().getValue()%>/<%=position.getStartDate().getYear()%>
                                    -
                                    <%=position.getEndDate().getMonth().getValue()%>/<%=position.getEndDate().getYear()%>
                                </td>
                                <td>
                                    <b><%=position.getTitle()%>
                                    </b><br>
                                    <c:choose>
                                        <c:when test="${empty position.description}">
                                        </c:when>
                                        <c:otherwise>
                                            ${position.description}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </table>
                    </c:forEach>
                </c:when>
            </c:choose>

        </c:forEach>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>