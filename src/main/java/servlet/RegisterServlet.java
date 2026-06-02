package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
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

        User user =
                new User(0, userName, password);

        UserDAO dao =
                new UserDAO();

        boolean result =
                dao.insertUser(user);

        if (result) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/jsp/login.jsp");
        } else {
            request.setAttribute(
                    "errorMsg",
                    "ユーザー登録に失敗しました");

            request.getRequestDispatcher(
                    "/jsp/register.jsp")
                    .forward(request, response);
        }
    }
}