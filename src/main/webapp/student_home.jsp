<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h2>Welcome, ${studentName}</h2> <!-- Show student name dynamically -->

<!-- Submit Assignment Section -->
<h3>Submit Assignment</h3>
<form action="StudentServlet" method="POST" enctype="multipart/form-data">
    <label for="assignment">Select Assignment:</label><br>
    <select name="assignment" id="assignment" required>
        <c:forEach var="assignment" items="${assignments}">
            <option value="${assignment.id}"><c:out value="${assignment.title}"/></option>
        </c:forEach>
    </select><br><br>

    <label for="file">Select File:</label><br>
    <input type="file" name="file" id="file" required><br><br>

    <button type="submit">Submit</button>
</form>

<h3>Your Previous Submissions</h3>
<ul>
    <c:if test="${not empty submissions}">
        <table border="1">
            <tr>
                <th>Assignment Title</th>
                <th>Submission Date</th>
                <th>Download</th>
            </tr>
            <c:forEach var="submission" items="${submissions}">
                <tr>
                    <td><c:out value="${submission.assignment.title}"/></td>
                    <td><c:out value="${submission.submissionTime}"/></td>
                    <td><a href="StudentServlet?download=${submission.id}">Download</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty submissions}">
        <li>You haven't submitted any assignments yet.</li>
    </c:if>
</ul>

<br>
<a href="logout">Logout</a>
</body>
</html>
