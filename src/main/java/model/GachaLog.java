package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

/**
 * ガチャの直近履歴をデータベース(gacha_logs)で永続管理するクラス
 */
public class GachaLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_HISTORY = 2; // 直近2件まで保持

    private List<Integer> recentIds = new ArrayList<>();
    private List<String> recentNames = new ArrayList<>();

    // コンストラクタ：インスタンス生成時にデータベースから直近2件を読み込む
    public GachaLog(int userId) {
        loadFromDatabase(userId);
    }

    /**
     * データベースから指定ユーザーの最新2件の履歴を読み込む
     */
    private void loadFromDatabase(int userId) {
        // SQLite用に最新2件を取得するクエリ
        String sql = 
            "SELECT gl.menu_id, m.menu_name " +
            "FROM gacha_logs gl " +
            "JOIN menus m ON gl.menu_id = m.menu_id " +
            "WHERE gl.user_id = ? " +
            "ORDER BY gl.drawn_at DESC " +
            "LIMIT ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);
            ps.setInt(2, MAX_HISTORY);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // 最新順(降順)で取得されるため、古いものが先頭にくるように0番目に追加していく
                    recentIds.add(0, rs.getInt("menu_id"));
                    recentNames.add(0, rs.getString("menu_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新しい履歴をデータベースに保存し、メモリ上のリストも更新する
     */
    public void saveToDatabase(int userId, int menuId, String menuName) {
        String sql = "INSERT INTO gacha_logs (user_id, menu_id) VALUES (?, ?)";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);
            ps.setInt(2, menuId);
            ps.executeUpdate();

            // メモリ上のアレイリストも最新状態に追従
            recentIds.add(menuId);
            recentNames.add(menuName);
            
            // 2件を超えたら古い履歴（先頭）をリストから取り除く
            if (recentIds.size() > MAX_HISTORY) {
                recentIds.remove(0);
                recentNames.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 該当なしの場合に、データベース上のこのユーザーの過去ログをリセットする
     */
    public void clearDatabaseLog(int userId) {
        String sql = "DELETE FROM gacha_logs WHERE user_id = ?";
        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            
            // メモリ上もクリア
            this.recentIds.clear();
            this.recentNames.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getRecentIds() {
        return this.recentIds;
    }

    public List<String> getRecentNames() {
        return this.recentNames;
    }
}