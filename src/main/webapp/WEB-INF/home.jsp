<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
</head>
<body>
<h2>Welcome, ${user.username}</h2> <!-- Welcome user by username -->
<p>Your role is: ${user.role}</p>
<!-- Provide different options for Student and Instructor -->
<c:choose>
    <c:when test="${user.role == 'STUDENT'}">
        <a href="submit_assignment.jsp">Submit Assignment</a>
    </c:when>
    <c:when test="${user.role == 'INSTRUCTOR'}">
        <a href="create_assignment.jsp">Create Assignment</a>
    </c:when>
</c:choose>
</body>
</html>
