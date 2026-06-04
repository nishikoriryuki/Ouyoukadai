<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c"
    uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ガチャ結果</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/result_gacha.css">
</head>

<body class="
    <c:choose>
        <c:when test='${kondate.difficulty == 1}'>
            easy-bg
        </c:when>
        <c:when test='${kondate.difficulty == 2}'>
            normal-bg
        </c:when>
        <c:otherwise>
            hard-bg
        </c:otherwise>
    </c:choose>
">

    <div class="intro">
        🍽 ${kondate.name}
    </div>

    <div class="card">
        <div class="kondate_ingredient">
            <p>🥕 材料：</p>
            <ul>
                <c:forEach var="ingredient" items="${kondate.ingredients}">
                    <li>${ingredient}</li>
                </c:forEach>
            </ul>
        </div>

        <div class="kondate_calorie">
            <p>
                🔥 1食分のカロリー：
                ${kondate.calorie} kcal
            </p>
        </div>

        <div class="kondate_difficulty">
            <c:choose>
                <c:when test="${kondate.difficulty == 1}">
                    <p class="easy">
                        🟢 難易度：簡単
                    </p>
                </c:when>
                <c:when test="${kondate.difficulty == 2}">
                    <p class="normal">
                        🟠 難易度：普通
                    </p>
                </c:when>
                <c:otherwise>
                    <p class="hard">
                        🔴 難易度：難しい
                    </p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <form id="back-gacha-form" action="<%= request.getContextPath() %>/jsp/gacha.jsp" method="POST" style="display:none;">
        <c:forEach var="id" items="${selectedAllergies}">
            <input type="hidden" name="prevAllergies" value="${id}">
        </c:forEach>
    </form>

    <p style="text-align: center; margin-top: 20px;">
        <a href="javascript:void(0);" onclick="document.getElementById('back-gacha-form').submit();">
            もう一度ガチャを回す
        </a>
    </p>

</body>
</html>