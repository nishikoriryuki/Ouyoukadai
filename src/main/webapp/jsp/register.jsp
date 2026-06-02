<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録</title>
</head>
<body>

<h1>ユーザー登録</h1>

<form action="<%= request.getContextPath() %>/RegisterServlet"
      method="post">

    <p>
        ユーザー名：
        <input type="text" name="userName">
    </p>

    <p>
        パスワード：
        <input type="password" name="password">
    </p>

    <button type="submit">登録</button>

</form>

<p>
    <a href="<%= request.getContextPath() %>/jsp/login.jsp">
        ログイン画面へ
    </a>
</p>

</body>
</html>