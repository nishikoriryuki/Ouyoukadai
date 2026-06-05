package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Kondate;
import util.DBUtil;

public class KondateDAO {

    public Kondate chooseRandomKondate(
            String[] allergyIds,
            List<Integer> recentIds) {

        Kondate kondate = null;
        StringBuilder sql = new StringBuilder();

        List<Integer> validAllergyIds = new ArrayList<>();

        if (allergyIds != null) {
            for (String idStr : allergyIds) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    validAllergyIds.add(Integer.parseInt(idStr));
                }
            }
        }

        if (validAllergyIds.isEmpty()) {
            sql.append("SELECT * FROM menus WHERE 1=1 ");
        } else {
            StringBuilder placeholders = new StringBuilder();

            for (int i = 0; i < validAllergyIds.size(); i++) {
                placeholders.append("?");

                if (i < validAllergyIds.size() - 1) {
                    placeholders.append(",");
                }
            }

            sql.append("SELECT DISTINCT m.* FROM menus m ")
               .append("WHERE m.menu_id NOT IN ( ")
               .append("    SELECT mi.menu_id ")
               .append("    FROM menu_ingredients mi ")
               .append("    JOIN ingredient_allergens ia ")
               .append("    ON mi.ingredient_id = ia.ingredient_id ")
               .append("    WHERE ia.allergen_id IN (")
               .append(placeholders)
               .append(") ")
               .append(") ");
        }

        if (recentIds != null && !recentIds.isEmpty()) {
            sql.append("AND menu_id NOT IN (");

            for (int i = 0; i < recentIds.size(); i++) {
                sql.append("?");

                if (i < recentIds.size() - 1) {
                    sql.append(",");
                }
            }

            sql.append(") ");
        }

        sql.append("ORDER BY RANDOM() LIMIT 1");

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
        ) {

            int index = 1;

            for (Integer allergyId : validAllergyIds) {
                ps.setInt(index++, allergyId);
            }

            if (recentIds != null && !recentIds.isEmpty()) {
                for (Integer recentId : recentIds) {
                    ps.setInt(index++, recentId);
                }
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                kondate = createKondateFromResultSet(rs, conn);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kondate;
    }

    public Kondate chooseRandomKondateByDifficulty(
            int difficulty,
            String[] allergyIds,
            List<Integer> recentIds) {

        Kondate kondate = null;
        StringBuilder sql = new StringBuilder();

        List<Integer> validAllergyIds = new ArrayList<>();

        if (allergyIds != null) {
            for (String idStr : allergyIds) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    validAllergyIds.add(Integer.parseInt(idStr));
                }
            }
        }

        if (validAllergyIds.isEmpty()) {
            sql.append("SELECT * FROM menus WHERE difficulty = ? ");
        } else {
            StringBuilder placeholders = new StringBuilder();

            for (int i = 0; i < validAllergyIds.size(); i++) {
                placeholders.append("?");

                if (i < validAllergyIds.size() - 1) {
                    placeholders.append(",");
                }
            }

            sql.append("SELECT DISTINCT m.* FROM menus m ")
               .append("WHERE m.difficulty = ? ")
               .append("AND m.menu_id NOT IN ( ")
               .append("    SELECT mi.menu_id ")
               .append("    FROM menu_ingredients mi ")
               .append("    JOIN ingredient_allergens ia ")
               .append("    ON mi.ingredient_id = ia.ingredient_id ")
               .append("    WHERE ia.allergen_id IN (")
               .append(placeholders)
               .append(") ")
               .append(") ");
        }

        if (recentIds != null && !recentIds.isEmpty()) {
            sql.append("AND menu_id NOT IN (");

            for (int i = 0; i < recentIds.size(); i++) {
                sql.append("?");

                if (i < recentIds.size() - 1) {
                    sql.append(",");
                }
            }

            sql.append(") ");
        }

        sql.append("ORDER BY RANDOM() LIMIT 1");

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
        ) {

            int index = 1;

            ps.setInt(index++, difficulty);

            for (Integer allergyId : validAllergyIds) {
                ps.setInt(index++, allergyId);
            }

            if (recentIds != null && !recentIds.isEmpty()) {
                for (Integer recentId : recentIds) {
                    ps.setInt(index++, recentId);
                }
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                kondate = createKondateFromResultSet(rs, conn);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kondate;
    }

    private Kondate createKondateFromResultSet(
            ResultSet rs,
            Connection conn) throws Exception {

        Kondate kondate = new Kondate();

        kondate.setId(rs.getInt("menu_id"));
        kondate.setName(rs.getString("menu_name"));
        kondate.setCalorie(rs.getInt("calorie"));
        kondate.setDifficulty(rs.getInt("difficulty"));
        kondate.setImageUrl(rs.getString("image_url"));

        String ingredientSql =
                "SELECT i.ingredient_name " +
                "FROM ingredients i " +
                "JOIN menu_ingredients mi " +
                "ON i.ingredient_id = mi.ingredient_id " +
                "WHERE mi.menu_id = ?";

        try (PreparedStatement ingredientPs =
                     conn.prepareStatement(ingredientSql)) {

            ingredientPs.setInt(1, kondate.getId());

            try (ResultSet ingredientRs = ingredientPs.executeQuery()) {
                List<String> ingredientList = new ArrayList<>();

                while (ingredientRs.next()) {
                    ingredientList.add(
                            ingredientRs.getString("ingredient_name"));
                }

                kondate.setIngredients(ingredientList);
            }
        }

        return kondate;
    }
}