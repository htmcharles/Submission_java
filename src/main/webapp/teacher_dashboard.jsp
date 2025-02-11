<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.app.online_submission.model.Assignment" %>
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
</head>
<body>

<h2>Teacher Dashboard</h2>
<a href="logout.jsp">Logout</a>

<h3>Create New Assignment</h3>
<form action="CreateAssignmentServlet" method="post">
    <label for="title">Assignment Title:</label><br>
    <input type="text" id="title" name="title" required><br><br>

    <label for="description">Description:</label><br>
    <textarea id="description" name="description"></textarea><br><br>

    <label for="courseId">Select Course:</label><br>
    <select id="courseId" name="courseId">
        <%-- Populate this list dynamically with courses from the database --%>
        <option value="1">Course 1</option>
        <option value="2">Course 2</option>
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
                <th>COURSEID</th>
                <th>INSTRUCTOR</th>
                <th>DEADLINE</th>
            </tr>
            <c:forEach var="assignment" items="${assignments}">
                <tr>
                    <td><c:out value="${assignment.id}"/></td>
                    <td><c:out value="${assignment.title}"/></td>
                    <td><c:out value="${assignment.description}"/></td>
                    <td><c:out value="${assignment.course.id}"/></td>
                    <td><c:out value="${assignment.instructor.id}"/></td>
                    <td><c:out value="${assignment.deadline}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty assignments}">
        <li>No assignments created yet.</li>
    </c:if>
</ul>

</body>
</html>
