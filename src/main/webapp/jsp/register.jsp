<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規登録</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/css/register.css">

</head>
<body>

<div class="register-container">

    <div class="register-title">
        🍳 ユーザー登録
    </div>

    <form action="<%=request.getContextPath()%>/RegisterServlet"
          method="post">

        <div class="form-group">
            <label>ユーザー名</label>
            <input type="text"
                   name="userName"
                   required>
        </div>

        <div class="form-group">
            <label>パスワード</label>
            <input type="password"
                   name="password"
                   required>
        </div>

        <button type="submit"
                class="register-btn">
            登録する
        </button>

    </form>

    <div class="link-area">

        <a href="<%=request.getContextPath()%>/jsp/login.jsp">
            ログイン画面へ戻る
        </a>

    </div>

</div>

</body>
</html>