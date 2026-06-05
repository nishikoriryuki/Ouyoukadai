package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.KondateDAO;
import model.Kondate;

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

        KondateDAO dao = new KondateDAO();

        String[] allergyIds =
                request.getParameterValues("allergies");

        if (allergyIds == null) {
            allergyIds = new String[0];
        }

        System.out.print("選択されたアレルギーID: ");
        if (allergyIds.length == 0) {
            System.out.println("なし");
        } else {
            System.out.println(String.join(", ", allergyIds));
        }

        // gacha.jspから受け取る
        String effectType =
                request.getParameter("effectType");

        String fixedDifficultyParam =
                request.getParameter("fixedDifficulty");

        System.out.println("演出タイプ：" + effectType);
        System.out.println("確定難易度：" + fixedDifficultyParam);

        Kondate kondate;

        if (fixedDifficultyParam != null
                && !fixedDifficultyParam.isEmpty()) {

            int difficulty =
                    Integer.parseInt(fixedDifficultyParam);

            kondate =
                    dao.chooseRandomKondateByDifficulty(
                            difficulty,
                            allergyIds
                    );

        } else {

            kondate =
                    dao.chooseRandomKondate(allergyIds);
        }

        if (kondate != null) {
            System.out.println("選ばれた献立：" + kondate.getName());
            System.out.println("難易度：" + kondate.getDifficulty());
        } else {
            System.out.println("該当する献立がありませんでした。");
        }

        request.setAttribute("kondate", kondate);
        request.setAttribute("effectType", effectType);
        request.setAttribute("selectedAllergies", allergyIds);

        RequestDispatcher rd =
                request.getRequestDispatcher(
                        "/WEB-INF/result_gacha.jsp"
                );

        rd.forward(request, response);
    }
}