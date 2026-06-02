package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;
import util.DBUtil;

public class UserDAO {

    /**
     * ユーザー登録
     */
    public boolean insertUser(User user) {

        String sql =
                "INSERT INTO users(user_name, password) "
              + "VALUES(?, ?)";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());

            int result = pstmt.executeUpdate();

            return result == 1;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    /**
     * ログイン判定
     */
    public User login(String userName,
                      String password) {

        String sql =
                "SELECT * FROM users "
              + "WHERE user_name = ? "
              + "AND password = ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, userName);
            pstmt.setString(2, password);

            ResultSet rs =
                    pstmt.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setUserId(
                        rs.getInt("user_id"));

                user.setUserName(
                        rs.getString("user_name"));

                user.setPassword(
                        rs.getString("password"));

                return user;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}