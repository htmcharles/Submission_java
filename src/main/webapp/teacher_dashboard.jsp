<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("user") == null) {
        response.sendRedirect("login.jsp?error=session_expired");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<h2>Teacher Dashboard</h2>

<h3>Create New Assignment</h3>
<form action="TeacherServlet" method="post">
    <label for="title">Assignment Title:</label><br>
    <input type="text" id="title" name="title" required><br><br>

    <label for="description">Description:</label><br>
    <textarea id="description" name="description"></textarea><br><br>

    <label for="courseId">Select Course:</label><br>
    <select id="courseId" name="courseId">
        <c:forEach var="course" items="${courses}">
            <option value="${course.id}">${course.name}</option>
        </c:forEach>
    </select><br><br>

    <label for="deadline">Deadline:</label><br>
    <input type="datetime-local" id="deadline" name="deadline" required><br><br>

    <button type="submit">Create Assignment</button>
</form>

<h3>Manage Assignments</h3>
<ul>
    <c:if test="${not empty assignments}">
        <table>
            <tr>
                <th>ID</th>
                <th>TITLE</th>
                <th>DESCRIPTION</th>
                <th>COURSE</th>
                <th>DEADLINE</th>
            </tr>
            <c:forEach var="assignment" items="${assignments}">
                <tr>
                    <td><c:out value="${assignment.id}"/></td>
                    <td><c:out value="${assignment.title}"/></td>
                    <td><c:out value="${assignment.description}"/></td>
                    <td>
                        <c:forEach var="course" items="${courses}">
                            <c:if test="${course.id == assignment.course.id}">
                                <c:out value="${course.name}"/>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td><c:out value="${assignment.deadline}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty assignments}">
        <li>No assignments created yet.</li>
    </c:if>
</ul>

<h3>Manage Submissions</h3>
<c:if test="${not empty submissions}">
    <table>
        <tr>
            <th>Assignment Title</th>
            <th>Student Username</th>
            <th>Submission Time</th>
            <th>Status</th> <!-- New column for Status -->
            <th>Download</th>
        </tr>
        <c:forEach var="submission" items="${submissions}">
            <tr>
                <td><c:out value="${submission.assignment.title}"/></td>
                <td><c:out value="${submission.student.username}"/></td>
                <td><c:out value="${submission.submissionTime}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${submission.late}">
                            Late Submission
                        </c:when>
                        <c:otherwise>
                            On Time
                        </c:otherwise>
                    </c:choose>
                </td>
                <td><a href="StudentServlet?download=${submission.id}">Download</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${empty submissions}">
    <li>No submissions available.</li>
</c:if>
<a href="logout">Logout</a>
</body>
</html>
