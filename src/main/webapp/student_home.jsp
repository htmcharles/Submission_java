<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
</head>
<body>
<h2>Welcome, Student</h2>

<!-- Submit Assignment Section -->
<h3>Submit Assignment</h3>
<form action="StudentServlet" method="POST" enctype="multipart/form-data">
    <label for="assignment">Select Assignment:</label><br>
    <select name="assignment" id="assignment" required>
        <c:forEach var="assignment" items="${assignments}">
            <option value="${assignment.id}"><c:out value="${assignment.title}"/></option>
        </c:forEach>
    </select><br><br>

    <label for="studentId">Student ID:</label><br>
    <input type="text" name="studentId" id="studentId" required><br><br>

    <label for="file">Select File:</label><br>
    <input type="file" name="file" id="file" required><br><br>

    <button type="submit">Submit</button>
</form>

<!-- View Available Assignments Section -->
<h3>Available Assignments</h3>
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
                    <td><c:out value="${assignment.course.name}"/></td>
                    <td><c:out value="${assignment.deadline}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty assignments}">
        <li>No assignments created yet.</li>
    </c:if>
</ul>
<br>
<a href="logout.jsp">Logout</a>
</body>
</html>