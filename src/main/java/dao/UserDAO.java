package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;
import util.DBUtil;
import util.PasswordUtil;

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
            String hashedPassword =
                    PasswordUtil.hashPassword(user.getPassword());

            pstmt.setString(2, hashedPassword);

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
              + "WHERE user_name = ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, userName);

            ResultSet rs =
                    pstmt.executeQuery();

            if (rs.next()) {

                String storedPassword =
                        rs.getString("password");

                if (!PasswordUtil.checkPassword(
                        password,
                        storedPassword)) {

                    return null;
                }

                User user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setPassword(storedPassword);

                return user;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
    
    public boolean existsUserName(String userName) {

        String sql =
                "SELECT COUNT(*) FROM users "
              + "WHERE user_name = ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, userName);

            ResultSet rs =
                    pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}