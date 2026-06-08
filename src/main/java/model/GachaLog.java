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

        String sql =
            "SELECT gl.menu_id, m.menu_name " +
            "FROM gacha_logs gl " +
            "JOIN menus m ON gl.menu_id = m.menu_id " +
            "WHERE gl.user_id = ? " +
            "ORDER BY gl.log_id DESC";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int menuId = rs.getInt("menu_id");
                    String menuName = rs.getString("menu_name");

                    // すでに同じIDが入っていたらスキップ
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

     * 新しい履歴をデータベースに保存し、最新2件を残して古いログを自動で削除する

     */

    public void saveToDatabase(int userId, int menuId, String menuName) {

        String insertSql = "INSERT INTO gacha_logs (user_id, menu_id) VALUES (?, ?)";

        // ★SQLiteで動く「最新2件以外を削除する」SQL

        // 「自分の履歴のうち、最新の2件のdrawn_at」よりも古い日時のデータを削除します

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

            // 自動コミットをオフにして、追加と削除をセット（トランザクション）で行う

            conn.setAutoCommit(false);
 
            // 1. 新しい履歴の追加

            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

                psInsert.setInt(1, userId);

                psInsert.setInt(2, menuId);

                psInsert.executeUpdate();

            }
 
            // 2. 最新2件を残して古いデータを削除

            try (PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {

                psDelete.setInt(1, userId);

                psDelete.setInt(2, userId);

                psDelete.setInt(3, MAX_HISTORY);

                psDelete.executeUpdate();

            }
 
            // 確定

            conn.commit();
 
            // メモリ上のアレイリストも最新状態に追従

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

    public List<Integer> getRecentIds() {

        return this.recentIds;

    }
 
    public List<String> getRecentNames() {

        return this.recentNames;

    }
    
    public void clearDatabaseLog(int userId) {

        String sql = "DELETE FROM gacha_logs WHERE user_id = ?";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
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

 