function playGachaEffet(event) {
    // 画面遷移をいったんストップ
    event.preventDefault();
    
    const audio = document.getElementById('gacha-audio');
    const button = document.getElementById('gacha-button');
    const form = document.getElementById('gacha-form');
    
    // ボタンを無効化してテキストを変更
    if (button) {
        button.disabled = true;
        button.innerText = "ガチャを回中...";
    }
    
    // 音を鳴らす
    if (audio) {
        audio.currentTime = 0;
        audio.play().catch(error => {
            console.log("再生エラー:", error);
            form.submit();
        });
    } else {
        form.submit();
    }
    
    // 3秒待ってからサーブレットへ送信して画面切り替え
    setTimeout(() => {
        form.submit();
    }, 3000); 
}