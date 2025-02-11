<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
<h2>Login</h2>

<!-- Display error message if login fails -->
<% String error = request.getParameter("error"); %>
<% if (error != null) { %>
<p style="color:red;">
    <% if ("invalid_credentials".equals(error)) { %>
    Invalid username or password.
    <% } else if ("invalid_role".equals(error)) { %>
    User role not recognized.
    <% } else if ("session_expired".equals(error)) { %>
    Your session has expired. Please log in again.
    <% } %>
</p>
<% } %>

<!-- Display logout message -->
<% String logout = request.getParameter("logout"); %>
<% if (logout != null && logout.equals("true")) { %>
<p style="color:green;">You have successfully logged out.</p>
<% } %>

<form action="login" method="POST">
    <label for="username">Username:</label><br>
    <input type="text" name="username" id="username" required><br><br>

    <label for="password">Password:</label><br>
    <input type="password" name="password" id="password" required><br><br>

    <button type="submit">Login</button>
</form>
</body>
</html>
