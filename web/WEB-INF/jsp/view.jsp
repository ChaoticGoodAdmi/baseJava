<%--suppress XmlPathReference --%>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"
                                                                                      alt="Edit"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
        <c:set var="sectionType" value="${sectionEntry.key}"/>
        <jsp:useBean id="sectionType" type="com.urise.webapp.model.SectionType"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <jsp:useBean id="section" type="com.urise.webapp.model.Section"/>
        <h3>
            <%=sectionType.getTitle()%>
        </h3>
        <dd>
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
            </c:choose>
        </dd>
    </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>