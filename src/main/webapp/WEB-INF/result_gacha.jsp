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

    <link rel="stylesheet"
        href="<%= request.getContextPath() %>/css/result_gacha.css">
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

    <!-- デバッグ用 -->
    <div style="display:none;">
        name=${kondate.name}
        difficulty=${kondate.difficulty}
        calorie=${kondate.calorie}
        imageUrl=${kondate.imageUrl}
    </div>

    <!-- 料理名 -->
    <div class="intro">
        🍽 ${kondate.name}
    </div>

    <!-- 料理画像 -->
    <div class="food-image">

        <img
            src="<%= request.getContextPath() %>/images/${kondate.imageUrl}"
            alt="${kondate.name}">

    </div>

    <!-- カード -->
    <div class="card">

        <!-- 材料 -->
        <div class="kondate_ingredient">

            <p>🥕 材料：</p>

            <ul>

                <c:forEach var="ingredient"
                    items="${kondate.ingredients}">

                    <li>${ingredient}</li>

                </c:forEach>

            </ul>

        </div>

        <!-- カロリー -->
        <div class="kondate_calorie">

            <p>
                🔥 1食分のカロリー：
                ${kondate.calorie} kcal
            </p>

        </div>

        <!-- 難易度 -->
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

    <!-- 戻るフォーム -->
    <form id="back-gacha-form"
        action="<%= request.getContextPath() %>/jsp/gacha.jsp"
        method="POST"
        style="display:none;">

        <c:forEach var="id"
            items="${selectedAllergies}">

            <input
                type="hidden"
                name="prevAllergies"
                value="${id}">

        </c:forEach>

    </form>

    <!-- 戻るリンク -->
    <p style="text-align:center; margin-top:20px;">

        <a href="javascript:void(0);"
            onclick="document.getElementById('back-gacha-form').submit();">

            もう一度ガチャを回す

        </a>

    </p>

</body>

</html>