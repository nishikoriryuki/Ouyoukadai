package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;
import util.DBUtil;

public class UserDAO {

    public User chooseRandomUser() {

        User user = null;

        String sql =
                "SELECT * FROM users " +
                "ORDER BY RAND() " +
                "LIMIT 1";

        try (
            Connection conn =
                    DBUtil.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();
        ) {

            if (rs.next()) {

                user = new User();

                user.setId(
                        rs.getInt("id")
                );

                user.setStudentNumber(
                        rs.getInt("student_number")
                );

                user.setName(
                        rs.getString("name")
                );

                user.setKana(
                        rs.getString("kana")
                );

                user.setBirthDate(
                        rs.getString("birth_date")
                );

                user.setHometown(
                        rs.getString("hometown")
                );

                user.setSchool(
                        rs.getString("school")
                );

                user.setHobby(
                        rs.getString("hobby")
                );

                user.setRarity(
                        rs.getString("rarity")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return user;
    }
}