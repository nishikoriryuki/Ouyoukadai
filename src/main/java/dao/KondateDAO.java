package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Kondate;
import util.DBUtil;

public class KondateDAO {

    public Kondate chooseRandomKondate() {

        Kondate kondate = null;

        String sql =
                "SELECT * FROM kondate " +
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

                kondate = new Kondate();

                kondate.setId(
                        rs.getInt("id")
                );

                kondate.setName(
                        rs.getString("name")
                );

                kondate.setCalorie(
                        rs.getInt("calorie")
                );

                kondate.setIngredient(
                        rs.getString("ingredient")
                );

                kondate.setDifficulty(
                        rs.getString("difficulty")
                );

                kondate.setImage(
                        rs.getString("image")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return kondate;
    }
}