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

        // アレルギーIDテスト
        String[] allergyIds = {"2"};

        // gacha.jspから受け取る
        String isGoldParam =
                request.getParameter("isGold");

        boolean isGold =
                "true".equals(isGoldParam);

        System.out.println(
                "金カプセル判定：" + isGold);

        Kondate kondate;

        if (isGold) {

            kondate = dao.chooseRandomDifficultKondate(
                    allergyIds);

        } else {

            kondate = dao.chooseRandomKondate(
                    allergyIds);
        }
        
        System.out.println(
                "難易度：" +
                kondate.getDifficulty());

        request.setAttribute(
                "kondate",
                kondate);

        request.setAttribute(
                "isGold",
                isGold);

        RequestDispatcher rd =
                request.getRequestDispatcher(
                        "/WEB-INF/result_gacha.jsp");

        rd.forward(
                request,
                response);
    }
}