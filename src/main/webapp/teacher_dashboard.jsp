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
<form action="createAssignment" method="post">
    <label for="title">Assignment Title:</label><br>
    <input type="text" id="title" name="title" required><br><br>

    <label for="description">Description:</label><br>
    <textarea id="description" name="description"></textarea><br><br>

    <label for="courseId">Select Course:</label><br>
    <select id="courseId" name="courseId">
        <%-- Populate this list with courses from the database --%>
        <option value="1">Course 1</option>
        <option value="2">Course 2</option>
    </select><br><br>

    <label for="deadline">Deadline:</label><br>
    <input type="datetime-local" id="deadline" name="deadline" required><br><br>

    <button type="submit">Create Assignment</button>
</form>

<h3>Manage Assignments</h3>
<ul>
    <%
        List<Assignment> assignments = (List<Assignment>) request.getAttribute("assignments");
        if (assignments != null && !assignments.isEmpty()) {
            for (Assignment assignment : assignments) {
    %>
    <li>
        <strong>Course:</strong> <%= assignment.getCourse().getName() %><br>
        <strong>Title:</strong> <%= assignment.getTitle() %><br>
        <strong>Deadline:</strong> <%= assignment.getDeadline() %><br>
        <strong>Description:</strong> <%= assignment.getDescription() != null ? assignment.getDescription() : "No description" %><br>
        <a href="viewSubmissions?assignmentId=<%= assignment.getId() %>">View Submissions</a>
    </li>
    <hr>
    <%
        }
    } else {
    %>
    <li>No assignments created yet.</li>
    <%
        }
    %>
</ul>

</body>
</html>
