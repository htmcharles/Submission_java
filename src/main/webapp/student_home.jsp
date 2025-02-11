<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.app.online_submission.model.Assignment" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    // Check if session exists and if user is logged in
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("user") == null) {
        response.sendRedirect("login.jsp?error=session_expired");
        return;
    }

    // Retrieve the list of assignments passed from the servlet
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

<!-- View Submitted Assignments Section -->
<h3>Submitted Assignments</h3>
<ul>
    <%
        if (assignments != null && !assignments.isEmpty()) {
            for (Assignment assignment : assignments) {
    %>
    <li>
        <b>Subject:</b> <%= assignment.getTitle() %><br>
        <b>Description:</b> <%= assignment.getDescription() != null ? assignment.getDescription() : "No description" %><br>
        <b>Deadline:</b> <%= assignment.getDeadline() %><br>
    </li>
    <%
        }
    } else {
    %>
    <li>No assignments found.</li>
    <%
        }
    %>
</ul>

<!-- Check Deadlines Section (if needed) -->
<h3>Assignment Deadlines</h3>
<ul>
    <!-- Example static data; replace with actual dynamic data if needed -->
    <li>Math Assignment - Due: 2025-02-20</li>
    <li>Science Project - Due: 2025-02-25</li>
</ul>

<br>
<a href="logout.jsp">Logout</a>
</body>
</html>
