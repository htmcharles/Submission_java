<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.app.online_submission.model.Assignment" %>
<%@ page import="com.app.online_submission.model.Course" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Assignment> assignments = (List<Assignment>) request.getAttribute("assignments");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
</head>
<body>

<h2>Student Dashboard</h2>
<a href="logout.jsp">Logout</a>

<h3>Uploaded Assignments</h3>
<ul>
    <%
        if (assignments != null && !assignments.isEmpty()) {
            for (Assignment assignment : assignments) {
    %>
    <li>
        <strong>Course:</strong> <%= assignment.getCourse().getName() %><br>
        <strong>Title:</strong> <%= assignment.getTitle() %><br>
        <strong>Deadline:</strong> <%= assignment.getDeadline() %><br>
        <strong>Description:</strong> <%= assignment.getDescription() != null ? assignment.getDescription() : "No description" %>
    </li>
    <hr>
    <%
        }
    } else {
    %>
    <li>No assignments uploaded yet.</li>
    <%
        }
    %>
</ul>

<h3>Upload Assignment</h3>
<form action="uploadAssignment" method="post" enctype="multipart/form-data">
    <label for="title">Title:</label><br>
    <input type="text" id="title" name="title" required><br><br>

    <label for="description">Description:</label><br>
    <textarea id="description" name="description"></textarea><br><br>

    <label for="file">Select File:</label><br>
    <input type="file" id="file" name="file" required><br><br>

    <button type="submit">Upload</button>
</form>

</body>
</html>
