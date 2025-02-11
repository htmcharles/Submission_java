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
<h2>Welcome, Teacher</h2>

<!-- View Student Submissions -->
<h3>Student Submissions</h3>
<ul>
    <%
        List<Assignment> submissions = (List<Assignment>) request.getAttribute("submissions");
        if (submissions != null) {
            for (Assignment submission : submissions) {
    %>
    <li>
        <%= submission.getStudentName() %> - <%= submission.getSubject() %>
        <a href="download?file=<%= submission.getFilePath() %>">Download</a>
    </li>
    <%
        }
    } else {
    %>
    <li>No submissions available.</li>
    <%
        }
    %>
</ul>

<!-- Grade Assignments -->
<h3>Grade Assignment</h3>
<form action="gradeAssignment" method="POST">
    <label for="student">Student Name:</label><br>
    <input type="text" name="student" id="student" required><br><br>

    <label for="subject">Subject:</label><br>
    <input type="text" name="subject" id="subject" required><br><br>

    <label for="grade">Grade:</label><br>
    <input type="text" name="grade" id="grade" required><br><br>

    <button type="submit">Submit Grade</button>
</form>

<br>
<a href="logout.jsp">Logout</a>
</body>
</html>
