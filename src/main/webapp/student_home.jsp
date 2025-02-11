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
    <title>Student Dashboard</title>
</head>
<body>
<h2>Welcome, Student</h2>

<!-- Upload Assignment -->
<h3>Upload Assignment</h3>
<form action="uploadAssignment" method="POST" enctype="multipart/form-data">
    <label for="file">Select File:</label><br>
    <input type="file" name="file" id="file" required><br><br>

    <label for="subject">Subject:</label><br>
    <input type="text" name="subject" id="subject" required><br><br>

    <button type="submit">Upload</button>
</form>

<!-- View Submitted Assignments -->
<h3>Submitted Assignments</h3>
<ul>
    <%
        List<Assignment> assignments = (List<Assignment>) request.getAttribute("assignments");
        if (assignments != null) {
            for (Assignment assignment : assignments) {
    %>
    <li><%= assignment.getSubject() %> - Uploaded on <%= assignment.getUploadDate() %></li>
    <%
        }
    } else {
    %>
    <li>No assignments submitted yet.</li>
    <%
        }
    %>
</ul>

<!-- Check Deadlines -->
<h3>Assignment Deadlines</h3>
<ul>
    <li>Math Assignment - Due: 2025-02-20</li>
    <li>Science Project - Due: 2025-02-25</li>
</ul>

<br>
<a href="logout.jsp">Logout</a>
</body>
</html>
