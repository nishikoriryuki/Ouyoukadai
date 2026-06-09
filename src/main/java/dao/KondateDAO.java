package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Kondate;
import util.DBUtil;

public class KondateDAO {

    /**
     * アレルギーと直近履歴を除外して、
     * ランダムに献立を1件取得する
     */
    public Kondate chooseRandomKondate(
            String[] allergyIds,
            List<Integer> recentIds) {

        Kondate kondate = null;
        StringBuilder sql = new StringBuilder();

        // 有効なアレルギーIDだけをリスト化
        List<Integer> validAllergyIds =
                createValidAllergyIds(allergyIds);

        // =========================
        // SQL作成：アレルギー除外
        // =========================

        if (validAllergyIds.isEmpty()) {

            sql.append("SELECT * FROM menus WHERE 1=1 ");

        } else {

            StringBuilder placeholders =
                    createPlaceholders(validAllergyIds.size());

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

        // =========================
        // SQL作成：直近履歴除外
        // =========================

        if (recentIds != null && !recentIds.isEmpty()) {

            StringBuilder placeholders =
                    createPlaceholders(recentIds.size());

            sql.append("AND menu_id NOT IN (")
               .append(placeholders)
               .append(") ");
        }

        // ランダムに1件取得
        sql.append("ORDER BY RANDOM() LIMIT 1");

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql.toString())
        ) {

            int index = 1;

            // アレルギーIDをSQLにセット
            for (Integer allergyId : validAllergyIds) {
                ps.setInt(index++, allergyId);
            }

            // 直近履歴IDをSQLにセット
            if (recentIds != null && !recentIds.isEmpty()) {

                for (Integer recentId : recentIds) {
                    ps.setInt(index++, recentId);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    kondate =
                            createKondateFromResultSet(
                                    rs,
                                    conn);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return kondate;
    }

    /**
     * 指定された難易度の中から、
     * アレルギーと直近履歴を除外して
     * ランダムに献立を1件取得する
     */
    public Kondate chooseRandomKondateByDifficulty(
            int difficulty,
            String[] allergyIds,
            List<Integer> recentIds) {

        Kondate kondate = null;
        StringBuilder sql = new StringBuilder();

        // 有効なアレルギーIDだけをリスト化
        List<Integer> validAllergyIds =
                createValidAllergyIds(allergyIds);

        // =========================
        // SQL作成：難易度指定 + アレルギー除外
        // =========================

        if (validAllergyIds.isEmpty()) {

            sql.append(
                    "SELECT * FROM menus WHERE difficulty = ? ");

        } else {

            StringBuilder placeholders =
                    createPlaceholders(validAllergyIds.size());

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

        // =========================
        // SQL作成：直近履歴除外
        // =========================

        if (recentIds != null && !recentIds.isEmpty()) {

            StringBuilder placeholders =
                    createPlaceholders(recentIds.size());

            sql.append("AND menu_id NOT IN (")
               .append(placeholders)
               .append(") ");
        }

        // ランダムに1件取得
        sql.append("ORDER BY RANDOM() LIMIT 1");

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql.toString())
        ) {

            int index = 1;

            // 難易度をSQLにセット
            ps.setInt(index++, difficulty);

            // アレルギーIDをSQLにセット
            for (Integer allergyId : validAllergyIds) {
                ps.setInt(index++, allergyId);
            }

            // 直近履歴IDをSQLにセット
            if (recentIds != null && !recentIds.isEmpty()) {

                for (Integer recentId : recentIds) {
                    ps.setInt(index++, recentId);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    kondate =
                            createKondateFromResultSet(
                                    rs,
                                    conn);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return kondate;
    }

    /**
     * 画面から送られてきたアレルギーIDのうち、
     * 有効な値だけをInteger型のリストに変換する
     */
    private List<Integer> createValidAllergyIds(String[] allergyIds) {

        List<Integer> validAllergyIds = new ArrayList<>();

        if (allergyIds != null) {

            for (String idStr : allergyIds) {

                if (idStr != null && !idStr.trim().isEmpty()) {
                    validAllergyIds.add(
                            Integer.parseInt(idStr));
                }
            }
        }

        return validAllergyIds;
    }

    /**
     * SQLのIN句で使うプレースホルダーを作成する
     *
     * 例：
     * count = 3 の場合 → ?,?,?
     */
    private StringBuilder createPlaceholders(int count) {

        StringBuilder placeholders = new StringBuilder();

        for (int i = 0; i < count; i++) {

            placeholders.append("?");

            if (i < count - 1) {
                placeholders.append(",");
            }
        }

        return placeholders;
    }

    /**
     * ResultSetの内容からKondateオブジェクトを作成する
     *
     * menusテーブルの情報に加えて、
     * 対応する食材一覧も取得する
     */
    private Kondate createKondateFromResultSet(
            ResultSet rs,
            Connection conn)
            throws Exception {

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

        try (
            PreparedStatement ingredientPs =
                    conn.prepareStatement(ingredientSql)
        ) {

            ingredientPs.setInt(1, kondate.getId());

            try (
                ResultSet ingredientRs =
                        ingredientPs.executeQuery()
            ) {

                List<String> ingredientList =
                        new ArrayList<>();

                while (ingredientRs.next()) {

                    ingredientList.add(
                            ingredientRs.getString(
                                    "ingredient_name"));
                }

                kondate.setIngredients(ingredientList);
            }
        }

        return kondate;
    }
}