document.addEventListener("DOMContentLoaded", function () {

    function showKakuteiText() {
        const kakuteiText = document.getElementById("kakutei-text");

        if (kakuteiText) {
            kakuteiText.classList.remove("show");
            void kakuteiText.offsetWidth;
            kakuteiText.classList.add("show");
        }
    }

    // サイドバー開閉
    const toggleBtn = document.getElementById("toggle-btn");
    const sidebar = document.getElementById("sidebar");

    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener("click", function (event) {
            sidebar.classList.toggle("open");

            if (sidebar.classList.contains("open")) {
                toggleBtn.textContent = "閉じる ✖";
            } else {
                toggleBtn.textContent = "アレルギー設定 ⚙";
            }

            event.stopPropagation();
        });

        document.body.addEventListener("click", function (e) {
            if (!sidebar.contains(e.target) && sidebar.classList.contains("open")) {
                sidebar.classList.remove("open");
                toggleBtn.textContent = "アレルギー設定 ⚙";
            }
        });
    }

    let isSpinning = false;

    const handle = document.getElementById("handle");

    if (handle) {
        handle.addEventListener("click", function (event) {
            playGachaEffect(event);
        });
    }

    function playGachaEffect(event) {
        event.preventDefault();

        if (isSpinning) return;
        isSpinning = true;

        const gachaAudio = document.getElementById("gacha-audio");
        const chanceAudio = document.getElementById("chance-audio");
        const button = document.getElementById("gacha-button");
        const form = document.getElementById("gacha-form");
        const c1ColorElement = document.getElementById("c1-color");
        const c1TopElement = document.getElementById("c1-top");

        if (button) {
            button.disabled = true;
            button.innerText = "ガチャを回し中...";
        }

        const colors = ["st7", "st8", "st9", "st10", "st11", "st12"];

        colors.forEach(function (className) {
            c1ColorElement.classList.remove(className);
        });

        c1ColorElement.classList.remove("gold-capsule");
        c1ColorElement.removeAttribute("fill");
        c1TopElement.removeAttribute("fill");
        document.body.classList.remove("rainbow-bg");

        const rand = Math.random();

        if (rand < 0.01) {
            // 星4確定 全部演出
            document.getElementById("fixedDifficulty").value = "4";
            document.getElementById("effectType").value = "ALL_EFFECTS";

            playChanceAudio(chanceAudio);
            showKakuteiText();

            document.body.classList.add("rainbow-bg");

            c1ColorElement.setAttribute("fill", "url(#goldGradient)");
            c1TopElement.setAttribute("fill", "#FEE9A0");
            c1ColorElement.classList.add("gold-capsule");

        } else if (rand < 0.09) {
            // 星3以上確定
            const difficulty = Math.random() < 0.8 ? 3 : 4;

            document.getElementById("fixedDifficulty").value = difficulty;
            document.getElementById("effectType").value = "RANDOM_EFFECT";

            const randomEffect = Math.floor(Math.random() * 4);

            if (randomEffect === 0) {
                c1ColorElement.setAttribute("fill", "url(#goldGradient)");
                c1TopElement.setAttribute("fill", "#FEE9A0");
                c1ColorElement.classList.add("gold-capsule");

            } else if (randomEffect === 1) {
                document.body.classList.add("rainbow-bg");

            } else if (randomEffect === 2) {
                playChanceAudio(chanceAudio);

            } else {
                showKakuteiText();
            }

        } else {
            // 通常
            document.getElementById("fixedDifficulty").value = "";
            document.getElementById("effectType").value = "NORMAL";

            const randomColor = colors[Math.floor(Math.random() * colors.length)];
            c1ColorElement.classList.add(randomColor);
        }

        // ガチャ本体の回転
        document.getElementById("gacha-wrapper").classList.add("act");

        // ガチャ音
        setTimeout(function () {
            if (gachaAudio) {
                gachaAudio.currentTime = 0.2;
                gachaAudio.volume = 0.3;
                gachaAudio.play().catch(function (error) {
                    console.log("ガチャ音エラー:", error);
                });
            }
        }, 500);

        // 3秒後に送信
        setTimeout(function () {
            form.submit();
        }, 3000);
    }

    function playChanceAudio(chanceAudio) {
        if (chanceAudio) {
            chanceAudio.currentTime = 0;
            chanceAudio.volume = 0.45;
            chanceAudio.play().catch(function (error) {
                console.log("先バレ音エラー:", error);
            });
        }
    }
});