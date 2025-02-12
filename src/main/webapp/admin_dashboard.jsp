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
    <title>Admin Dashboard</title>
</head>
<body>
<h2>Admin Dashboard</h2>
<a href="logout.jsp">Logout</a>

<!-- Display Messages -->
<c:if test="${not empty successMessage}">
    <p style="color: green;"><c:out value="${successMessage}" /></p>
</c:if>
<c:if test="${not empty errorMessage}">
    <p style="color: red;"><c:out value="${errorMessage}" /></p>
</c:if>

<!-- Add User Form -->
<h3>Add New User</h3>
<form action="AdminServlet" method="post">
    <label>Username:</label>
    <input type="text" name="username" required><br><br>

    <label>Password:</label>
    <input type="password" name="password" required><br><br>

    <label>Role:</label>
    <select name="role" required>
        <option value="STUDENT">Student</option>
        <option value="TEACHER">Teacher</option>
    </select><br><br>

    <button type="submit">Add User</button>
</form>
<!-- Add Course Form -->
<h3>Add New Course</h3>
<form action="AdminServlet" method="post">
    <label>Course Name:</label>
    <input type="text" name="courseName" required><br><br>

    <label>Course Description:</label>
    <textarea name="courseDescription" required></textarea><br><br>

    <label>Instructor ID:</label>
    <input type="text" name="instructorId" required><br><br>

    <button type="submit">Add Course</button>
</form>



<!-- View All Assignments -->
<h3>All Assignments</h3>
<c:if test="${not empty assignments}">
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Course</th>
            <th>Deadline</th>
        </tr>
        <c:forEach var="assignment" items="${assignments}">
            <tr>
                <td><c:out value="${assignment.id}"/></td>
                <td><c:out value="${assignment.title}"/></td>
                <td><c:out value="${assignment.description}"/></td>
                <td><c:out value="${assignment.course.name}"/></td>
                <td><c:out value="${assignment.deadline}"/></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${empty assignments}">
    <p>No assignments available.</p>
</c:if>

<!-- View All Submissions -->
<h3>All Submissions</h3>
<c:if test="${not empty submissions}">
    <table border="1">
        <tr>
            <th>Assignment Title</th>
            <th>Student ID</th>
            <th>Submission Date</th>
            <th>File</th>
            <th>Status</th>
        </tr>
        <c:forEach var="submission" items="${submissions}">
            <tr>
                <td><c:out value="${submission.assignment.title}"/></td>
                <td><c:out value="${submission.student.id}"/></td>
                <td><c:out value="${submission.submissionTime}"/></td>
                <td><a href="${submission.filePath}" target="_blank">View File</a></td>
                <td>
                    <c:choose>
                        <c:when test="${submission.submissionTime != null}">
                            Submitted
                        </c:when>
                        <c:otherwise>
                            Not Submitted
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${empty submissions}">
    <p>No submissions available.</p>
</c:if>
</body>
</html>
