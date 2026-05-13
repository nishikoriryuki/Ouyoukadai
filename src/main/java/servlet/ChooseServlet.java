package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.KondateDAO;
import model.Kondate;

@WebServlet("/choose")
public class ChooseServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        KondateDAO dao = new KondateDAO();

        Kondate kondate = dao.chooseRandomKondate();

        request.setAttribute("kondate", kondate);

        request.getRequestDispatcher(
                "/jsp/result_gacha.jsp"
        ).forward(request, response);
    }
}