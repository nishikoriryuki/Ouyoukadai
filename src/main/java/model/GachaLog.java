package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

/**
 * ガチャの直近履歴を
 * データベース（gacha_logs）で管理するクラス
 */
public class GachaLog implements Serializable {

    private static final long serialVersionUID = 1L;

    // 直近2件まで保持
    private static final int MAX_HISTORY = 2;

    private List<Integer> recentIds = new ArrayList<>();
    private List<String> recentNames = new ArrayList<>();

    /**
     * コンストラクタ
     *
     * インスタンス生成時に、
     * 指定ユーザーの直近履歴をデータベースから読み込む
     */
    public GachaLog(int userId) {

        loadFromDatabase(userId);
    }

    /**
     * データベースから
     * 指定ユーザーの最新2件の履歴を読み込む
     */
    private void loadFromDatabase(int userId) {

        String sql =
                "SELECT gl.menu_id, m.menu_name " +
                "FROM gacha_logs gl " +
                "JOIN menus m ON gl.menu_id = m.menu_id " +
                "WHERE gl.user_id = ? " +
                "ORDER BY gl.log_id DESC";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int menuId =
                            rs.getInt("menu_id");

                    String menuName =
                            rs.getString("menu_name");

                    // すでに同じ献立IDがある場合はスキップ
                    if (recentIds.contains(menuId)) {
                        continue;
                    }

                    recentIds.add(0, menuId);
                    recentNames.add(0, menuName);

                    // 重複なしで2件集まったら終了
                    if (recentIds.size() >= MAX_HISTORY) {
                        break;
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 新しい履歴をデータベースに保存する
     *
     * 保存後、最新2件を残して
     * 古い履歴は削除する
     */
    public void saveToDatabase(
            int userId,
            int menuId,
            String menuName) {

        String insertSql =
                "INSERT INTO gacha_logs (user_id, menu_id) " +
                "VALUES (?, ?)";

        // 最新2件以外の履歴を削除するSQL
        String deleteSql =
                "DELETE FROM gacha_logs " +
                "WHERE user_id = ? " +
                "AND log_id NOT IN ( " +
                "    SELECT log_id FROM gacha_logs " +
                "    WHERE user_id = ? " +
                "    ORDER BY log_id DESC " +
                "    LIMIT ? " +
                ")";

        try (Connection conn = DBUtil.getConnection()) {

            // 追加と削除をまとめて実行するため、
            // 自動コミットをオフにする
            conn.setAutoCommit(false);

            // =========================
            // 1. 新しい履歴を追加
            // =========================

            try (
                PreparedStatement psInsert =
                        conn.prepareStatement(insertSql)
            ) {

                psInsert.setInt(1, userId);
                psInsert.setInt(2, menuId);

                psInsert.executeUpdate();
            }

            // =========================
            // 2. 古い履歴を削除
            // =========================

            try (
                PreparedStatement psDelete =
                        conn.prepareStatement(deleteSql)
            ) {

                psDelete.setInt(1, userId);
                psDelete.setInt(2, userId);
                psDelete.setInt(3, MAX_HISTORY);

                psDelete.executeUpdate();
            }

            // 追加と削除を確定
            conn.commit();

            // メモリ上の履歴も更新
            recentIds.add(menuId);
            recentNames.add(menuName);

            if (recentIds.size() > 2) {

                recentIds.remove(0);
                recentNames.remove(0);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 直近で排出された献立IDリストを取得する
     */
    public List<Integer> getRecentIds() {

        return this.recentIds;
    }

    /**
     * 直近で排出された献立名リストを取得する
     */
    public List<String> getRecentNames() {

        return this.recentNames;
    }

    /**
     * 指定ユーザーのガチャ履歴を
     * データベースとメモリ上の両方から削除する
     */
    public void clearDatabaseLog(int userId) {

        String sql =
                "DELETE FROM gacha_logs WHERE user_id = ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            recentIds.clear();
            recentNames.clear();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}