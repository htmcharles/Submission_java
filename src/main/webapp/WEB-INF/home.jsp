<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="com.app.online_submission.model.Submission" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <!-- Include your CSS files here -->
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<header>
    <h1>Welcome to the Student Dashboard</h1>
    <nav>
        <a href="profile.jsp">Profile</a>
        <a href="assignments.jsp">Assignments</a>
        <a href="logout.jsp">Logout</a>
    </nav>
</header>

<main>
    <section>
        <h2>Your Assignments</h2>

        <!-- Check if assignments list is not empty -->
        <c:if test="${not empty submissions}">
            <ul>
                <!-- Iterate through each submission -->
                <c:forEach var="submission" items="${submissions}"><% Submission submission; %>
                    <li>
                        <b>Assignment Title:</b> <%= submission.getAssignment().getTitle() %><br>
                        <b>Course:</b> <%= submission.getAssignment().getCourse().getName() %><br>
                        <b>Uploaded on:</b>
                        <%= submission.getUploadDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(submission.getUploadDate()) : "Not Uploaded Yet" %>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <!-- Message if there are no submissions -->
        <c:if test="${empty submissions}">
            <p>No assignments submitted yet.</p>
        </c:if>
    </section>

</main>

<footer>
    <p>&copy; 2025 Online Submission System</p>
</footer>

</body>
</html>
