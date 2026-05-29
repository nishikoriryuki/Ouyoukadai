<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c"
    uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html lang="ja">

<head>

    <meta charset="UTF-8">

    <title>ガチャ結果</title>

    <link rel="stylesheet"
        href="<%= request.getContextPath() %>/css/result_gacha.css">

</head>

<body>

    <div class="card">

        <div class="kondate_name">
            <p>料理名：${kondate.name}</p>
        </div>
        
        <div class="kondate_ingredient">
            <p>材料：</p>
            <ul>
                <c:forEach var="ingredient"
                   items="${kondate.ingredients}">
                   <li>${ingredient}</li>
               </c:forEach>
            </ul>
        </div>

        <div class="kondate_calorie">
            <p>1食分のカロリー：
                ${kondate.calorie} kcal
            </p>
        </div>

        <div class="kondate_difficulty">

            <p>
                難易度：

                <c:choose>

                    <c:when test="${kondate.difficulty == 1}">
                        簡単
                    </c:when>

                    <c:when test="${kondate.difficulty == 2}">
                        普通
                    </c:when>

                    <c:otherwise>
                        難しい
                    </c:otherwise>

                </c:choose>

            </p>

        </div>

    </div>

    <p>
        <a href="<%= request.getContextPath() %>/jsp/gacha.jsp">
            もどる
        </a>
    </p>

</body>

</html>