<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
</head>
<body>

<h1>ログイン</h1>

<p style="color:red;">
    ${errorMsg}
</p>

<form action="<%= request.getContextPath() %>/LoginServlet"
      method="post">

    <p>
        ユーザー名：
        <input type="text" name="userName">
    </p>

    <p>
        パスワード：
        <input type="password" name="password">
    </p>

    <button type="submit">ログイン</button>

</form>

<p>
    <a href="<%= request.getContextPath() %>/jsp/register.jsp">
        新規登録はこちら
    </a>
</p>

</body>
</html>