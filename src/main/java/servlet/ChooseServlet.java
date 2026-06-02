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
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        KondateDAO dao = new KondateDAO();

        // ★修正ポイント：JSPのチェックボックス（name="allergies"）の選択値を動的に取得
        String[] allergyIds = request.getParameterValues("allergies");

        // ★安全対策：何もチェックされていない場合は null になるので、空の配列を代入する
        if (allergyIds == null) {
            allergyIds = new String[0];
        }

        // （デバッグ用）受け取ったアレルギーIDをコンソールで確認
        System.out.print("選択されたアレルギーID: ");
        if (allergyIds.length == 0) {
            System.out.println("なし");
        } else {
            System.out.println(String.join(", ", allergyIds));
        }

        // gacha.jspから受け取る
        String isGoldParam = request.getParameter("isGold");
        boolean isGold = "true".equals(isGoldParam);

        System.out.println("金カプセル判定：" + isGold);

        Kondate kondate;

        // 動的に取得した allergyIds をそのままDAOに渡す
        if (isGold) {
            kondate = dao.chooseRandomDifficultKondate(allergyIds);
        } else {
            kondate = dao.chooseRandomKondate(allergyIds);
        }
        
        // ※もしデータが見つからなかった場合（除外されすぎて献立が0件になった時など）の考慮
        if (kondate != null) {
            System.out.println("難易度：" + kondate.getDifficulty());
        } else {
            System.out.println("該当する献立がありませんでした。");
        }

        request.setAttribute("kondate", kondate);
        request.setAttribute("isGold", isGold);
        
     // 選択されたアレルギーID配列をJSPに渡す
        request.setAttribute("selectedAllergies", allergyIds); 

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/result_gacha.jsp");
        rd.forward(request, response);

        
    }
}