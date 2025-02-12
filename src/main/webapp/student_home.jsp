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

<!-- Upload Assignment Section -->
<h3>Upload Assignment</h3>
<form action="uploadAssignment" method="POST" enctype="multipart/form-data">
    <label for="file">Select File:</label><br>
    <input type="file" name="file" id="file" required><br><br>

    <label for="subject">Subject:</label><br>
    <input type="text" name="subject" id="subject" required><br><br>

    <button type="submit">Upload</button>
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
<br>
<a href="logout.jsp">Logout</a>
</body>
</html>
