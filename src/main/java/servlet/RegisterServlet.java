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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // リクエストの文字コードを設定
        request.setCharacterEncoding("UTF-8");

        // 入力されたユーザー情報を取得
        String userName =
                request.getParameter("userName");

        String password =
                request.getParameter("password");

        // Userオブジェクトを生成
        User user =
                new User(
                        0,
                        userName,
                        password);

        UserDAO dao = new UserDAO();

        // =========================
        // ユーザー名重複チェック
        // =========================

        if (dao.existsUserName(userName)) {

            request.setAttribute(
                    "errorMsg",
                    "そのユーザー名はすでに使われています");

            request.getRequestDispatcher(
                    "/jsp/register.jsp")
                    .forward(request, response);

            return;
        }

        // =========================
        // ユーザー登録処理
        // =========================

        boolean result =
                dao.insertUser(user);

        // =========================
        // 登録成功
        // =========================

        if (result) {

            // ログイン画面へ遷移
            response.sendRedirect(
                    request.getContextPath()
                    + "/jsp/login.jsp");

        } else {

            // =========================
            // 登録失敗
            // =========================

            request.setAttribute(
                    "errorMsg",
                    "ユーザー登録に失敗しました");

            request.getRequestDispatcher(
                    "/jsp/register.jsp")
                    .forward(request, response);
        }
    }
}