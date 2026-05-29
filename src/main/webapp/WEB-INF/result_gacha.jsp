<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>ガチャ結果</title>
        <li rel="stylesheet" href="<%= request.getContextPath() %>/css/result_gacha.css">
    </head>
    <body>
        <div class="card">
            <div class="kondate_name">
               <p>料理名：${kondate.name}</p>
            </div>
            
            <div class="kondate_image">
               <p>料理名：${kondate.image}</p>
            </div>
            
            <div class="kondate_calorie">
               <p>1食分のカロリー：${kondate.calorie}kcal</p>
            </div>
            
            <div class="kondate_ingredient">
               <p>材料：${kondate.ingredient}</p>
            </div>
            
            <div class="kondate_difficulty">
               <p>難易度：${kondate.difficulty}</p>
            </div>
        </div>
            <p><a href="<%= request.getContextPath() %>/jsp/gacha.jsp">もどる</a></p>
    </body>
</html>