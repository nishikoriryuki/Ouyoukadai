<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/css/login.css">

</head>
<body>

<div class="login-container">

    <div class="login-title">
        🎲 今日の献立ガチャ
    </div>
    
    <p style="color:red;">
        ${errorMsg}
    </p>

    <form action="<%=request.getContextPath()%>/LoginServlet"
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

        <button class="login-btn"
                type="submit">

            ログイン
        </button>

    </form>

    <div class="link-area">

        <a href="<%=request.getContextPath()%>/jsp/register.jsp">
            新規登録はこちら
        </a>

    </div>

</div>

</body>
</html>