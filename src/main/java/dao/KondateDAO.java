package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Kondate;
import util.DBUtil;

public class KondateDAO {

    // ★修正：引数に List<Integer> recentIds を追加
    public Kondate chooseRandomKondate(String[] allergyIds, List<Integer> recentIds) {

        Kondate kondate = null;
        StringBuilder sql = new StringBuilder(); // SQLを動的に組み立てるため StringBuilder に変更

        List<Integer> validAllergyIds = new ArrayList<>();
        if (allergyIds != null) {
            for (String idStr : allergyIds) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    validAllergyIds.add(Integer.parseInt(idStr));
                }
            }
        }

        // --- アレルギー除外の条件組み立て ---
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

            sql.append("SELECT DISTINCT m.* FROM menus m WHERE m.menu_id NOT IN ( ")
               .append("    SELECT mi.menu_id FROM menu_ingredients mi ")
               .append("    JOIN ingredient_allergens ia ON mi.ingredient_id = ia.ingredient_id ")
               .append("    WHERE ia.allergen_id IN (").append(placeholders).append(") ")
               .append(") ");
        }

        // ★【追加】直近に引いた料理のIDリスト（recentIds）があれば、それも除外する条件を追加
        if (recentIds != null && !recentIds.isEmpty()) {
            sql.append(" AND menu_id NOT IN (");
            for (int i = 0; i < recentIds.size(); i++) {
                sql.append(recentIds.get(i));
                if (i < recentIds.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(") ");
        }

        // 最後にランダムに並び替えて1件取得する
        sql.append("ORDER BY RANDOM() LIMIT 1");

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
        ) {

            if (!validAllergyIds.isEmpty()) {
                for (int i = 0; i < validAllergyIds.size(); i++) {
                    ps.setInt(i + 1, validAllergyIds.get(i));
                }
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                kondate = new Kondate();
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

                try (PreparedStatement ingredientPs = conn.prepareStatement(ingredientSql)) {
                    ingredientPs.setInt(1, kondate.getId());

                    try (ResultSet ingredientRs = ingredientPs.executeQuery()) {
                        List<String> ingredientList = new ArrayList<>();
                        while (ingredientRs.next()) {
                            ingredientList.add(ingredientRs.getString("ingredient_name"));
                        }
                        kondate.setIngredients(ingredientList);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kondate;
    }

    // ★修正：引数に List<Integer> recentIds を追加
    public Kondate chooseRandomDifficultKondate(String[] allergyIds, List<Integer> recentIds) {

        Kondate kondate = null;
        StringBuilder sql = new StringBuilder(); // SQLを動的に組み立てるため StringBuilder に変更

        List<Integer> validAllergyIds = new ArrayList<>();
        if (allergyIds != null) {
            for (String idStr : allergyIds) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    validAllergyIds.add(Integer.parseInt(idStr));
                }
            }
        }

        // --- アレルギー除外の条件組み立て ---
        if (validAllergyIds.isEmpty()) {
            sql.append("SELECT * FROM menus WHERE difficulty = 3 ");
        } else {
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < validAllergyIds.size(); i++) {
                placeholders.append("?");
                if (i < validAllergyIds.size() - 1) {
                    placeholders.append(",");
                }
            }

            sql.append("SELECT DISTINCT m.* FROM menus m WHERE m.difficulty = 3 ")
               .append("AND m.menu_id NOT IN ( ")
               .append("    SELECT mi.menu_id FROM menu_ingredients mi ")
               .append("    JOIN ingredient_allergens ia ON mi.ingredient_id = ia.ingredient_id ")
               .append("    WHERE ia.allergen_id IN (").append(placeholders).append(") ")
               .append(") ");
        }

        // ★【追加】直近に引いた料理のIDリスト（recentIds）があれば、それも除外する条件を追加
        if (recentIds != null && !recentIds.isEmpty()) {
            sql.append(" AND menu_id NOT IN (");
            for (int i = 0; i < recentIds.size(); i++) {
                sql.append(recentIds.get(i));
                if (i < recentIds.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(") ");
        }

        // 最後にランダムに並び替えて1件取得する
        sql.append("ORDER BY RANDOM() LIMIT 1");

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
        ) {

            if (!validAllergyIds.isEmpty()) {
                for (int i = 0; i < validAllergyIds.size(); i++) {
                    ps.setInt(i + 1, validAllergyIds.get(i));
                }
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                kondate = new Kondate();
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

                try (PreparedStatement ingredientPs = conn.prepareStatement(ingredientSql)) {
                    ingredientPs.setInt(1, kondate.getId());

                    try (ResultSet ingredientRs = ingredientPs.executeQuery()) {
                        List<String> ingredientList = new ArrayList<>();
                        while (ingredientRs.next()) {
                            ingredientList.add(ingredientRs.getString("ingredient_name"));
                        }
                        kondate.setIngredients(ingredientList);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kondate;
    }
}