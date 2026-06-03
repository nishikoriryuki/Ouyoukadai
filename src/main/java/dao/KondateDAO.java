package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Kondate;
import util.DBUtil;

public class KondateDAO {

    public Kondate chooseRandomKondate(String[] allergyIds) {

        Kondate kondate = null;
        String sql;

        // ⭐【追加】空文字やnullを除外した有効なIDだけのリストを作成
        List<Integer> validAllergyIds = new ArrayList<>();
        if (allergyIds != null) {
            for (String idStr : allergyIds) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    validAllergyIds.add(Integer.parseInt(idStr));
                }
            }
        }

        // アレルギー未選択（有効なIDが1つもない場合）
        if (validAllergyIds.isEmpty()) {

            sql =
                "SELECT * FROM menus " +
                "ORDER BY RANDOM() " +
                "LIMIT 1";

        } else {

            StringBuilder placeholders = new StringBuilder();
            // ⭐ 有効なIDの数だけプレースホルダを生成
            for (int i = 0; i < validAllergyIds.size(); i++) {
                placeholders.append("?");
                if (i < validAllergyIds.size() - 1) {
                    placeholders.append(",");
                }
            }

            sql =
                "SELECT DISTINCT m.* " +
                "FROM menus m " +
                "WHERE m.menu_id NOT IN ( " +
                "    SELECT mi.menu_id " +
                "    FROM menu_ingredients mi " +
                "    JOIN ingredient_allergens ia " +
                "    ON mi.ingredient_id = ia.ingredient_id " +
                "    WHERE ia.allergen_id IN (" +
                        placeholders +
                ") ) " +
                "ORDER BY RANDOM() " +
                "LIMIT 1";
        }

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            // ⭐ プレースホルダへ値をセット（有効なリストから順に入れる）
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

                // 材料取得
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
    
    public Kondate chooseRandomDifficultKondate(String[] allergyIds) {

        Kondate kondate = null;
        String sql;

        // ⭐【追加】空文字やnullを除外した有効なIDだけのリストを作成
        List<Integer> validAllergyIds = new ArrayList<>();
        if (allergyIds != null) {
            for (String idStr : allergyIds) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    validAllergyIds.add(Integer.parseInt(idStr));
                }
            }
        }

        // アレルギー未選択（有効なIDが1つもない場合）
        if (validAllergyIds.isEmpty()) {

            sql =
                "SELECT * FROM menus " +
                "WHERE difficulty = 3 " +
                "ORDER BY RANDOM() " +
                "LIMIT 1";

        } else {

            StringBuilder placeholders = new StringBuilder();
            // ⭐ 有効なIDの数だけプレースホルダを生成
            for (int i = 0; i < validAllergyIds.size(); i++) {
                placeholders.append("?");
                if (i < validAllergyIds.size() - 1) {
                    placeholders.append(",");
                }
            }

            sql =
                "SELECT DISTINCT m.* " +
                "FROM menus m " +
                "WHERE m.difficulty = 3 " +
                "AND m.menu_id NOT IN ( " +
                "    SELECT mi.menu_id " +
                "    FROM menu_ingredients mi " +
                "    JOIN ingredient_allergens ia " +
                "    ON mi.ingredient_id = ia.ingredient_id " +
                "    WHERE ia.allergen_id IN (" +
                        placeholders +
                ") ) " +
                "ORDER BY RANDOM() " +
                "LIMIT 1";
        }

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            // ⭐ プレースホルダへ値をセット（有効なリストから順に入れる）
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