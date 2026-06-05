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
import model.User;

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

        // ログインユーザー取得
        User loginUser = (User) session.getAttribute("loginUser");

        int userId = 0;
        if (loginUser != null) {
            userId = loginUser.getUserId();
        }

        KondateDAO dao = new KondateDAO();

        // アレルギーID取得
        String[] allergyIds = request.getParameterValues("allergies");

        if (allergyIds == null) {
            allergyIds = new String[0];
        }

        System.out.print("選択されたアレルギーID: ");
        if (allergyIds.length == 0) {
            System.out.println("なし");
        } else {
            System.out.println(String.join(", ", allergyIds));
        }

        // gacha.jspから受け取る演出情報
        String effectType = request.getParameter("effectType");
        String fixedDifficultyParam = request.getParameter("fixedDifficulty");

        // DBから直近ログ取得
        GachaLog gachaLog = new GachaLog(userId);
        
        System.out.println("=================================");
        System.out.println("ユーザーID: " + userId);
        System.out.println("除外対象ID: " + gachaLog.getRecentIds());

        System.out.println("effectType = " + effectType);

        if (fixedDifficultyParam != null
                && !fixedDifficultyParam.isEmpty()) {

            System.out.println(
                    "確定演出発生 → difficulty="
                    + fixedDifficultyParam);

        } else {

            System.out.println("通常抽選");
        }

        System.out.println("=================================");

        Kondate kondate;

        // 確定難易度がある場合
        if (fixedDifficultyParam != null
                && !fixedDifficultyParam.isEmpty()) {

            int difficulty = Integer.parseInt(fixedDifficultyParam);

            kondate = dao.chooseRandomKondateByDifficulty(
                    difficulty,
                    allergyIds,
                    gachaLog.getRecentIds()
            );

        } else {

            kondate = dao.chooseRandomKondate(
                    allergyIds,
                    gachaLog.getRecentIds()
            );
        }

        // 除外条件が厳しすぎて0件になった場合、直近ログをリセットして再抽選
        if (kondate == null && !gachaLog.getRecentIds().isEmpty()) {

            System.out.println("該当なしのため、DB内の直近ログを一度リセットして再抽選します。");

            gachaLog.clearDatabaseLog(userId);

            if (fixedDifficultyParam != null
                    && !fixedDifficultyParam.isEmpty()) {

                int difficulty = Integer.parseInt(fixedDifficultyParam);

                kondate = dao.chooseRandomKondateByDifficulty(
                        difficulty,
                        allergyIds,
                        gachaLog.getRecentIds()
                );

            } else {

                kondate = dao.chooseRandomKondate(
                        allergyIds,
                        gachaLog.getRecentIds()
                );
            }
        }

        if (kondate != null) {

            if (kondate != null) {

                System.out.println("========== ガチャ結果 ==========");

                System.out.println(
                        "当選ID: "
                        + kondate.getId());

                System.out.println(
                        "当選料理: "
                        + kondate.getName());

                System.out.println(
                        "難易度: "
                        + kondate.getDifficulty());

                System.out.println(
                        "除外されていたID: "
                        + gachaLog.getRecentIds());

                System.out.println(
                        "effectType: "
                        + effectType);

                System.out.println(
                        "fixedDifficulty: "
                        + fixedDifficultyParam);

                System.out.println("===============================");

                gachaLog.saveToDatabase(
                        userId,
                        kondate.getId(),
                        kondate.getName());

            } else {

                System.out.println(
                        "該当する献立が完全にありませんでした。");
            }

            // 当選した料理をDBへ保存
            gachaLog.saveToDatabase(
                    userId,
                    kondate.getId(),
                    kondate.getName()
            );

        } else {
            System.out.println("該当する献立が完全にありませんでした。");
        }

        request.setAttribute("kondate", kondate);
        request.setAttribute("effectType", effectType);
        request.setAttribute("fixedDifficulty", fixedDifficultyParam);
        request.setAttribute("selectedAllergies", allergyIds);

        RequestDispatcher rd =
                request.getRequestDispatcher(
                        "/WEB-INF/result_gacha.jsp"
                );

        rd.forward(request, response);
    }
}