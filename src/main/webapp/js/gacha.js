function playGachaEffet(event) {
    event.preventDefault();

    const gachaAudio = document.getElementById('gacha-audio');
    const chanceAudio = document.getElementById('chance-audio');
    const button = document.getElementById('gacha-button');
    const form = document.getElementById('gacha-form');

    if (button) {
        button.disabled = true;
        button.innerText = "ガチャを回し中...";
    }

    const effectType = document.getElementById('effectType').value;

    // 確定演出なら先バレ音
    if (effectType === "ALL_EFFECTS" || effectType === "RANDOM_EFFECT") {
        if (chanceAudio) {
            chanceAudio.currentTime = 0;
            chanceAudio.volume = 0.45;
            chanceAudio.play().catch(error => {
                console.log("先バレ音エラー:", error);
            });
        }
    }

    // 少し後にガチャ音
    setTimeout(() => {
        if (gachaAudio) {
            gachaAudio.currentTime = 0;
            gachaAudio.volume = 0.3;
            gachaAudio.play().catch(error => {
                console.log("ガチャ音エラー:", error);
            });
        }
    }, 500);

    setTimeout(() => {
        form.submit();
    }, 3000);
}