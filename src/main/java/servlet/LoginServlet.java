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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // リクエストの文字コードを設定
        request.setCharacterEncoding("UTF-8");

        // ログインフォームから入力値を取得
        String userName =
                request.getParameter("userName");

        String password =
                request.getParameter("password");

        // ユーザー認証を実行
        UserDAO dao = new UserDAO();

        User loginUser =
                dao.login(userName, password);

        // =========================
        // ログイン成功
        // =========================
        if (loginUser != null) {

            // セッションを取得
            HttpSession session =
                    request.getSession();

            // ログインユーザー情報を保存
            session.setAttribute(
                    "loginUser",
                    loginUser);

            // ガチャ画面へリダイレクト
            response.sendRedirect(
                    request.getContextPath()
                    + "/jsp/gacha.jsp");

        } else {

            // =========================
            // ログイン失敗
            // =========================

            // エラーメッセージを設定
            request.setAttribute(
                    "errorMsg",
                    "ユーザー名またはパスワードが違います");

            // ログイン画面へ戻す
            request.getRequestDispatcher(
                    "/jsp/login.jsp")
                    .forward(request, response);
        }
    }
}