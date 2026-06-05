package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.KondateDAO;
import model.GachaLog;
import model.Kondate;
import model.User; // ★追加：Userクラスをインポート

@WebServlet("/ChooseServlet")
public class ChooseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public ChooseServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // =========================================================================
        // ★セッションからログインユーザー情報を取得
        // =========================================================================
        User loginUser = (User) session.getAttribute("loginUser"); 
        // ※もしログインサーブレット側でセットした名前が "user" などであれば、適宜書き換えてください。
        
        int userId = 0;
        if (loginUser != null) {
            userId = loginUser.getUserId(); // User.javaに定義されているゲッターメソッド
        }

        KondateDAO dao = new KondateDAO();

        // アレルギーIDの動的取得
        String[] allergyIds = request.getParameterValues("allergies");
        if (allergyIds == null) {
            allergyIds = new String[0];
        }

        // 金カプセル判定
        String isGoldParam = request.getParameter("isGold");
        boolean isGold = "true".equals(isGoldParam);

        // =========================================================================
        // ★【変更】データベースから、このユーザー専用の直近ログをロードする
        // =========================================================================
        GachaLog gachaLog = new GachaLog(userId);
        System.out.println("ユーザーID[" + userId + "] の除外ID（直近2件）: " + gachaLog.getRecentIds());
        // =========================================================================

        Kondate kondate;

        if (isGold) {
            kondate = dao.chooseRandomDifficultKondate(allergyIds, gachaLog.getRecentIds());
        } else {
            kondate = dao.chooseRandomKondate(allergyIds, gachaLog.getRecentIds());
        }
        
        // アレルギーや被り除外が厳しすぎて、該当する献立が0件になってしまった場合のセーフティ
        if (kondate == null && !gachaLog.getRecentIds().isEmpty()) {
            System.out.println("該当なしのため、DB内の直近ログを一度リセットして再抽選します。");
            gachaLog.clearDatabaseLog(userId); // ★DBの履歴を消去
            
            if (isGold) {
                kondate = dao.chooseRandomDifficultKondate(allergyIds, gachaLog.getRecentIds());
            } else {
                kondate = dao.chooseRandomKondate(allergyIds, gachaLog.getRecentIds());
            }
        }
        
        if (kondate != null) {
            System.out.println("当選した献立名：" + kondate.getName() + " (難易度：" + kondate.getDifficulty() + ")");
            
            // =========================================================================
            // ★【変更】当選した料理をデータベース（gacha_logs）へ保存する
            // =========================================================================
            gachaLog.saveToDatabase(userId, kondate.getId(), kondate.getName());
            // =========================================================================
        } else {
            System.out.println("該当する献立が完全にありませんでした。");
        }

        request.setAttribute("kondate", kondate);
        request.setAttribute("isGold", isGold);
        request.setAttribute("selectedAllergies", allergyIds); 

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/result_gacha.jsp");
        rd.forward(request, response);
    }
}