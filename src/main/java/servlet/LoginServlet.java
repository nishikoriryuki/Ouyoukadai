package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String userName =
                request.getParameter("userName");

        String password =
                request.getParameter("password");

        UserDAO dao =
                new UserDAO();

        User loginUser =
                dao.login(userName, password);

        if (loginUser != null) {

            HttpSession session =
                    request.getSession();

            session.setAttribute(
                    "loginUser",
                    loginUser);

            response.sendRedirect(
                    request.getContextPath()
                    + "/jsp/gacha.jsp");

        } else {

            request.setAttribute(
                    "errorMsg",
                    "ユーザー名またはパスワードが違います");

            request.getRequestDispatcher(
                    "/jsp/login.jsp")
                    .forward(request, response);
        }
    }
}