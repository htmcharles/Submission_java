<%@ page import="com.app.online_submission.model.User" %>
<%@ page session="true" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=session_expired");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Student Dashboard</title>
</head>
<body>
<h2>Welcome, <%= user.getUsername() %>!</h2>
<p>Role: <%= user.getRole() %></p>
<a href="logout">Logout</a> <!-- Logout link -->
</body>
</html>
