<%@ page import="model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
        response.sendRedirect(
            request.getContextPath() + "/jsp/login.jsp"
        );
        return;
    }
%>

<%@ taglib prefix="c" uri="jakarta.tags.core" %> 
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>今日の献立ガチャ</title>
    <link rel="stylesheet" href="../css/style.css?v=2">
    <link rel="stylesheet" href="../css/gacha.css">
    
    <style>
        /* --- 全体レイアウト（サイドバーのはみ出しを隠す設定） --- */
        body {
            margin: 0;
            padding: 0;
            overflow-x: hidden;
            position: relative;
            font-family: sans-serif;
        }

        @keyframes goldGlow {
            0% {
                filter: drop-shadow(0 0 8px #ffd700);
            }
            50% {
                filter: drop-shadow(0 0 30px #fff176);
            }
            100% {
                filter: drop-shadow(0 0 8px #ffd700);
            }
        }
        
        .gold-capsule {
            animation: goldGlow 0.8s infinite;
        }
        
        @keyframes rainbowBackground {
            0%   { background-position: 0% 50%; }
            50%  { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }
        
        .rainbow-bg {
            background: linear-gradient(
                90deg,
                #ff0000,
                #ff8800,
                #ffff00,
                #00ff00,
                #00ffff,
                #0000ff,
                #ff00ff,
                #ff0000
            );
        
            background-size: 400% 400%;
            animation: rainbowBackground 2s linear infinite;
        }
        
        .kakutei-text {
            display: none;
            overflow: hidden;
            position: absolute;
            top: 80px;
            left: 50%;
            transform: translateX(-50%);
        
            z-index: 999;
        
            padding: 14px 42px;
        
            font-size: 46px;
            font-weight: 900;
            letter-spacing: 6px;
        
            color: #fff;
        
            background:
                radial-gradient(circle at 50% 35%, #fff7b0 0%, transparent 35%),
                linear-gradient(135deg,
                    #7a0000 0%,
                    #ff0000 20%,
                    #ffcc00 45%,
                    #ffffff 50%,
                    #ffcc00 55%,
                    #ff0000 80%,
                    #7a0000 100%
                );
        
            border: 5px solid #fff2a8;
            border-radius: 12px;
        
            box-shadow:
                0 0 8px #fff,
                0 0 18px #ffcc00,
                0 0 35px #ff0000,
                0 0 60px rgba(255, 0, 0, 0.9);
        
            text-shadow:
                3px 3px 0 #8b0000,
                -2px -2px 0 #ffcc00,
                0 0 12px #fff,
                0 0 24px #ff0000;
        
            animation:
                pachinkoAppear 0.45s ease-out forwards,
                pachinkoFlash 0.18s infinite alternate,
                pachinkoShake 0.12s infinite;
        }
        
        .kakutei-text.show {
            display: block;
        }
        
        .kakutei-text::before {
            content: "";
            position: absolute;
            top: -80%;
            left: -40%;
        
            width: 35%;
            height: 260%;
        
            background: linear-gradient(
                90deg,
                transparent,
                rgba(255,255,255,0.9),
                transparent
            );
        
            transform: rotate(25deg);
            animation: pachinkoShine 0.8s infinite;
        }
        
        @keyframes pachinkoAppear {
            0% {
                opacity: 0;
                transform: translateX(-50%) scale(4) rotate(-8deg);
                filter: blur(8px);
            }
        
            60% {
                opacity: 1;
                transform: translateX(-50%) scale(0.85) rotate(4deg);
                filter: blur(0);
            }
        
            80% {
                transform: translateX(-50%) scale(1.15) rotate(-2deg);
            }
        
            100% {
                opacity: 1;
                transform: translateX(-50%) scale(1) rotate(0);
            }
        }
        
        @keyframes pachinkoFlash {
            from {
                filter: brightness(1);
            }
        
            to {
                filter: brightness(1.8);
            }
        }
        
        @keyframes pachinkoShake {
            0% {
                margin-left: 0;
            }
        
            25% {
                margin-left: -2px;
            }
        
            50% {
                margin-left: 2px;
            }
        
            75% {
                margin-left: -1px;
            }
        
            100% {
                margin-left: 0;
            }
        }
        
        @keyframes pachinkoShine {
            from {
                left: -40%;
            }
        
            to {
                left: 140%;
            }
        }
    </style>
    
    <script src="${pageContext.request.contextPath}/js/gacha.js"></script>
</head>

<body>

    <audio id="gacha-audio" src="${pageContext.request.contextPath}/audio/gacha.mp3" preload="auto"></audio>
    <audio id="chance-audio"
       src="${pageContext.request.contextPath}/audio/FOGバレ音.mp3"
       preload="auto"></audio>

    <div class="user-panel">
        <div class="user-name">
            ようこそ <%= loginUser.getUserName() %> さん
        </div>
        <a class="logout-link" href="<%= request.getContextPath() %>/LogoutServlet">
            ログアウト
        </a>
    </div>
    
    <button type="button" class="sidebar-toggle" id="toggle-btn">アレルギー設定 ⚙</button>

    <section class="wrapper" id="gacha-wrapper">
    
        <div id="kakutei-text" class="kakutei-text">確定!!</div>
        <div class="toy-wrap">
            <div class="toy">
                <svg viewBox="0 0 420 600" width="100%" height="100%">
                    <defs>
                        <linearGradient id="goldGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                            <stop offset="0%" stop-color="#B67B03"/>
                            <stop offset="45%" stop-color="#DAAF08"/>
                            <stop offset="70%" stop-color="#FEE9A0"/>
                            <stop offset="85%" stop-color="#DAAF08"/>
                            <stop offset="100%" stop-color="#B67B03"/>
                        </linearGradient>
                    </defs>
                
                    <g id="capsuletoy">
                        <path id="body" class="st0" d="M410,600H10c-5.5,0-10-4.5-10-10V50C0,22.4,22.4,0,50,0h320c27.6,0,50,22.4,50,50v540 C420,595.5,415.5,600,410,600z"/>
                        <path id="box" class="st1" d="M370,440H50c-27.6,0-50-22.4-50-50V70c0-27.6,22.4-50,50-50h320c27.6,0,50,22.4,50,50v320 C420,417.6,397.6,440,370,440z"/>
                        <path id="shadow" class="st2" d="M370,420H50c-27.6,0-50-22.4-50-50V50C0,22.4,22.4,0,50,0h320c27.6,0,50,22.4,50,50v320 C420,397.6,397.6,420,370,420z"/>
                        <path id="exit" class="st3" d="M115,460c-35.9,0-65,29.1-65,65v75h130v-75C180,489.1,150.9,460,115,460z"/>
                        <g id="coin">
                            <rect x="360" y="466" class="st4" width="38" height="76"/>
                            <rect x="374" y="476" class="st3" width="10" height="56"/>
                        </g>
                        <polygon class="st5" points="92.8,530 50,600 114.8,600 115,600 179.8,600 137,530"/>
                        <circle id="holder_x5F_back" class="st3" cx="290" cy="518" r="56"/>
                        <circle id="holder_x5F_front" class="st0" cx="290" cy="518" r="50"/>
                    </g>
                    
                    <g id="holder">
                        <path id="handle" class="st6" style="cursor: pointer;" d="M275.2,564.7c4.7,1.5,9.8,2.3,15,2.3s10.3-0.8,15-2.3v-95.4c-4.7-1.5-9.8-2.3-15-2.3 s-10.3,0.8-15,2.3V564.7z"/>
                    </g>
                    
                    <g id="c9">
                        <g>
                            <path class="st6" d="M305,115.7c-33.1,0-60,29.2-60,65.3c0,8.3,26.9,15,60,15s60-6.7,60-15C365,144.9,338.1,115.7,305,115.7z"/>
                            <ellipse class="st5" cx="305" cy="181" rx="60" ry="15"/>
                            <circle class="st0" cx="294" cy="173" r="37"/>
                            <path class="st7" d="M305,196c-33.1,0-60-6.7-60-15c0,47.7,26.9,58,60,58s60-10.3,60-58C365,189.3,338.1,196,305,196z"/>
                        </g>
                    </g>
                    <g id="c8">
                        <g>
                            <path class="st6" d="M203.6,230.9c23.4-23.4,21.8-63.1-3.7-88.6c-3.5-3.5-25.4,12.6-48.8,36.1s-39.6,45.3-36.1,48.8 C140.5,252.7,180.2,254.4,203.6,230.9z"/>
                            <ellipse transform="matrix(0.7071 -0.7071 0.7071 0.7071 -84.5545 165.4397)" class="st5" cx="157.4" cy="184.8" rx="60" ry="9"/>
                            <circle class="st0" cx="151.8" cy="196.1" r="37"/>
                            <path class="st8" d="M151.1,178.4c23.4-23.4,45.3-39.6,48.8-36.1c-33.7-33.7-60-22-83.4,1.4s-35.1,49.7-1.4,83.4 C111.5,223.7,127.6,201.9,151.1,178.4z"/>
                        </g>
                    </g>
                    <g id="c7">
                        <g>
                            <path class="st6" d="M41.6,302.7c-25.5-25.5-27.2-65.2-3.7-88.6s63.1-21.8,88.6,3.7L41.6,302.7z"/>
                            <circle class="st0" cx="84" cy="249" r="37"/>
                            <path class="st9" d="M73.4,249.7c-23.4,23.4-37.7,47.2-31.8,53c33.7,33.7,60,22,83.4-1.4s35.1-49.7,1.4-83.4 C120.6,212,96.8,226.3,73.4,249.7z"/>
                        </g>
                    </g>
                    <g id="c6">
                        <g>
                            <path class="st6" d="M286,263c0-36.1,26.9-65.3,60-65.3s60,29.2,60,65.3H286z"/>
                            <circle class="st0" cx="354" cy="255" r="37"/>
                            <path class="st9" d="M346,248c-33.1,0-60,6.7-60,15c0,47.7,26.9,58,60,58s60-10.3,60-58C406,254.7,379.1,248,346,248z"/>
                        </g>
                    </g>
                    <g id="c5">
                        <g>
                            <path class="st6" d="M248.8,175.5c-32.2-8-65.3,13.9-73.9,48.9c-2,8,22.5,21,54.6,29c32.2,8,59.9,7.9,61.8-0.1 C300,218.3,281,183.5,248.8,175.5z"/>
                            <ellipse transform="matrix(0.2404 -0.9707 0.9707 0.2404 -54.802 407.7581)" class="st5" cx="233.1" cy="238.9" rx="15" ry="60"/>
                            <circle class="st0" cx="224.4" cy="228.5" r="37"/>
                            <path class="st10" d="M229.5,253.5c-32.2-8-56.6-20.9-54.6-29c-11.5,46.3,12.1,62.8,44.3,70.7c32.2,8,60.7,4.4,72.2-41.9 C289.4,261.4,261.7,261.4,229.5,253.5z"/>
                        </g>
                    </g>
                    <g id="c4">
                        <g>
                            <path class="st6" d="M268,291.3c-19.4,26.9-11.3,65.8,17.9,86.8c4,2.9,23-16.5,42.4-43.4c19.4-26.9,31.8-51,27.7-54 C326.7,259.7,287.3,264.5,268,291.3z"/>
                            <ellipse transform="matrix(0.5842 -0.8116 0.8116 0.5842 -133.9644 397.4725)" class="st5" cx="321" cy="329.5" rx="60" ry="9"/>
                            <circle class="st0" cx="324.7" cy="317.4" r="37"/>
                            <path class="st8" d="M328.3,334.7c-19.4,26.9-38.3,46.3-42.4,43.4c38.7,27.8,62.8,12.1,82.1-14.8c19.4-26.9,26.7-54.7-12-82.6 C360,283.7,347.6,307.9,328.3,334.7z"/>
                        </g>
                    </g>
                    <g id="c3">
                        <g>
                            <path class="st6" d="M68.4,293.8c-23.4,23.4-21.8,63.1,3.7,88.6c5.9,5.9,29.6-8.4,53-31.8s37.7-47.2,31.8-53 C131.5,272,91.8,270.3,68.4,293.8z"/>
                            <ellipse transform="matrix(0.7071 -0.7071 0.7071 0.7071 -206.8116 180.5803)" class="st5" cx="114.6" cy="339.9" rx="60" ry="15"/>
                            <circle class="st0" cx="101.1" cy="342.1" r="37"/>
                            <path class="st11" d="M125.2,350.5c-23.4,23.4-47.2,37.7-53,31.8c33.7,33.7,60,22,83.4-1.4s35.1-49.7,1.4-83.4 C162.9,303.4,148.6,327.1,125.2,350.5z"/>
                        </g>
                    </g>
                    <g id="c2">
                        <g>
                            <path class="st6" d="M207,287.7c-33.1,0-60,29.2-60,65.3c0,5,26.9,9,60,9s60-4,60-9C267,316.9,240.1,287.7,207,287.7z"/>
                            <ellipse class="st5" cx="207" cy="353" rx="60" ry="9"/>
                            <circle class="st0" cx="207" cy="344" r="37"/>
                            <path class="st12" d="M207,362c-33.1,0-60-4-60-9c0,47.7,26.9,58,60,58s60-10.3,60-58C267,358,240.1,362,207,362z"/>
                        </g>
                    </g>
                    <g id="c1">
                        <g>
                            <path class="st6" d="M115,466.7c-33.1,0-60,29.2-60,65.3c0,5,26.9,9,60,9s60-4,60-9C175,495.9,148.1,466.7,115,466.7z"/>
                            <ellipse class="st5" cx="115" cy="532" rx="60" ry="9"/>
                            <circle id="c1-top" class="st0" cx="115" cy="523" r="37"/>
                            <path id="c1-color" class="st12" d="M115,541c-33.1,0-60-4-60-9c0,47.7,26.9,58,60,58s60-10.3,60-58C175,537,148.1,541,115,541z"/>
                        </g>
                    </g>
                    <g id="front">
                        <g>
                            <polygon class="st13" points="255,0 55,420 105,420 305,0"/>
                            <path class="st13" d="M50,0C22.4,0,0,22.4,0,50v320c0,14.3,6,27.1,15.6,36.2L209,0H50z"/>
                        </g>
                    </g>
                </svg>
            </div>
        </div>
    </section>

    <form id="gacha-form" method="POST" action="${pageContext.request.contextPath}/ChooseServlet">
        <input type="hidden" name="effectType" id="effectType">
        <input type="hidden" name="fixedDifficulty" id="fixedDifficulty">
        <input type="hidden" name="allergies" id="hiddenAllergies">

        <div class="sidebar" id="sidebar">
            <div class="sidebar-title">除外するアレルギー</div>
            <div class="allergy-list">
                
                <label class="allergy-item">
                    <c:set var="isChecked" value="false" />
                    <c:forEach var="prev" items="${selectedAllergies != null ? selectedAllergies : paramValues.prevAllergies}">
                        <c:if test="${prev == '1'}"><c:set var="isChecked" value="true" /></c:if>
                    </c:forEach>
                    <input type="checkbox" name="allergies" value="1" ${isChecked ? 'checked' : ''}>卵
                </label>

                <label class="allergy-item">
                    <c:set var="isChecked" value="false" />
                    <c:forEach var="prev" items="${selectedAllergies != null ? selectedAllergies : paramValues.prevAllergies}">
                        <c:if test="${prev == '2'}"><c:set var="isChecked" value="true" /></c:if>
                    </c:forEach>
                    <input type="checkbox" name="allergies" value="2" ${isChecked ? 'checked' : ''}>乳
                </label>

                <label class="allergy-item">
                    <c:set var="isChecked" value="false" />
                    <c:forEach var="prev" items="${selectedAllergies != null ? selectedAllergies : paramValues.prevAllergies}">
                        <c:if test="${prev == '3'}"><c:set var="isChecked" value="true" /></c:if>
                    </c:forEach>
                    <input type="checkbox" name="allergies" value="3" ${isChecked ? 'checked' : ''}>小麦
                </label>

                <label class="allergy-item">
                    <c:set var="isChecked" value="false" />
                    <c:forEach var="prev" items="${selectedAllergies != null ? selectedAllergies : paramValues.prevAllergies}">
                        <c:if test="${prev == '4'}"><c:set var="isChecked" value="true" /></c:if>
                    </c:forEach>
                    <input type="checkbox" name="allergies" value="4" ${isChecked ? 'checked' : ''}>えび
                </label>

                <label class="allergy-item">
                    <c:set var="isChecked" value="false" />
                    <c:forEach var="prev" items="${selectedAllergies != null ? selectedAllergies : paramValues.prevAllergies}">
                        <c:if test="${prev == '5'}"><c:set var="isChecked" value="true" /></c:if>
                    </c:forEach>
                    <input type="checkbox" name="allergies" value="5" ${isChecked ? 'checked' : ''}>大豆
                </label>

            </div>
        </div>
    </form>

    <script>
        function showKakuteiText() {
            const kakuteiText = document.getElementById("kakutei-text");
    
            if (kakuteiText) {
                kakuteiText.classList.remove("show");
                void kakuteiText.offsetWidth;
                kakuteiText.classList.add("show");
            }
        }
    
        // --- 1. サイドバーの開閉制御 ---
        const toggleBtn = document.getElementById('toggle-btn');
        const sidebar = document.getElementById('sidebar');

        toggleBtn.addEventListener('click', function(event) {
            sidebar.classList.toggle('open');
            if (sidebar.classList.contains('open')) {
                toggleBtn.textContent = '閉じる ✖';
            } else {
                toggleBtn.textContent = 'アレルギー設定 ⚙';
            }
            event.stopPropagation();
        });

        document.body.addEventListener('click', function(e) {
            if (!sidebar.contains(e.target) && sidebar.classList.contains('open')) {
                sidebar.classList.remove('open');
                toggleBtn.textContent = 'アレルギー設定 ⚙';
            }
        });

        // --- 2. 元のガチャの回転演出 ＆ 送信制御 ---
        let isSpinning = false;
        document.getElementById('handle').addEventListener('click', function() {
            if (isSpinning) return;
            isSpinning = true;

            const gachaAudio = document.getElementById('gacha-audio');
            if (gachaAudio) {
                gachaAudio.currentTime = 0.2;
                gachaAudio.volume = 0.3;
                gachaAudio.play().catch(error => console.log("音声再生エラー:", error));
            }
            

            const colors = ['st7', 'st8', 'st9', 'st10', 'st11', 'st12'];
            const c1ColorElement = document.getElementById('c1-color');

            colors.forEach(function(className) {
                c1ColorElement.classList.remove(className);
            });
            c1ColorElement.classList.remove('gold-capsule');

            const rand = Math.random();
            const chanceAudio = document.getElementById('chance-audio');

            if (rand < 0.99) {

                console.log("★★★★確定");

                if (chanceAudio) {
                    chanceAudio.currentTime = 0;
                    chanceAudio.play();
                }

                showKakuteiText();
                
                // 星4確定
                document.getElementById('fixedDifficulty').value = '4';
                document.getElementById('effectType').value = 'ALL_EFFECTS';

                document.body.classList.add("rainbow-bg");

                c1ColorElement.setAttribute("fill", "url(#goldGradient)");
                document.getElementById("c1-top").setAttribute("fill", "#FEE9A0");
                c1ColorElement.classList.add("gold-capsule");

            } else if (rand < 0.09) {

                console.log("★★★以上確定");

             // 星3以上確定
                const difficulty = Math.random() < 0.8 ? 3 : 4;

                document.getElementById('fixedDifficulty').value = difficulty;
                document.getElementById('effectType').value = 'RANDOM_EFFECT';

                const randomEffect = Math.floor(Math.random() * 4);

                if (randomEffect === 0) {
                    // 確定演出①：金カプセル
                    c1ColorElement.setAttribute("fill", "url(#goldGradient)");
                    document.getElementById("c1-top").setAttribute("fill", "#FEE9A0");
                    c1ColorElement.classList.add("gold-capsule");

                } else if (randomEffect === 1) {
                    // 確定演出②：虹背景
                    document.body.classList.add("rainbow-bg");

                } else if (randomEffect === 2){
                    // 確定演出③：先バレ音
                    if (chanceAudio) {
                        chanceAudio.currentTime = 0;
                        chanceAudio.play();
                    }
                } else {
                    // 確定演出④：確定文字
                    showKakuteiText();
                }

            } else {

                // 通常
                document.body.classList.remove("rainbow-bg");
                document.getElementById('fixedDifficulty').value = '';
                document.getElementById('effectType').value = 'NORMAL';

                c1ColorElement.classList.remove('gold-capsule');
                c1ColorElement.removeAttribute('fill');
                document.getElementById('c1-top').removeAttribute('fill');

                const randomColor =
                    colors[Math.floor(Math.random() * colors.length)];

                c1ColorElement.classList.add(randomColor);
            }
            
            document.getElementById('gacha-wrapper').classList.add('act');

            const checkedBoxes = document.querySelectorAll('input[name="allergies"]:checked');
            const checkedValues = Array.from(checkedBoxes).map(cb => cb.value);
            
            sidebar.style.display = 'block'; 
            
            setTimeout(function() {
                document.getElementById('gacha-form').submit();
            }, 3000);
        });
    </script>

</body>
</html>