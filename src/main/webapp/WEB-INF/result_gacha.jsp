<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>ガチャ結果</title>
    </head>
    <body>
        <p>料理名：${kondate.name}</p>
        <p>1食分のカロリー：${kondate.calorie}kcal</p>
        <p>材料：${kondate.ingredient}</p>
        <p>難易度：${kondate.difficulty}</p>
        <p><a href="<%= request.getContextPath() %>/jsp/gacha.jsp">もどる</a></p>
    </body>
</html>