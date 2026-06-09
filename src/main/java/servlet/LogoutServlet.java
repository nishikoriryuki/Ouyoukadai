package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // 既存のセッションを取得
        // （存在しない場合は新規作成しない）
        HttpSession session =
                request.getSession(false);

        // セッションが存在する場合は破棄
        if (session != null) {
            session.invalidate();
        }

        // ログイン画面へリダイレクト
        response.sendRedirect(
                request.getContextPath()
                + "/jsp/login.jsp");
    }
}