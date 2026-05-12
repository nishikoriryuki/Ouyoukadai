package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;

@WebServlet("/choose")
public class ChooseServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        UserDAO dao = new UserDAO();

        User user = dao.chooseRandomUser();

        request.setAttribute("user", user);

        request.getRequestDispatcher(
                "/jsp/result_gacha.jsp"
        ).forward(request, response);
    }
}