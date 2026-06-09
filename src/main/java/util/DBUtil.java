package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    // ローカル環境用のSQLite接続URL
    private static final String URL_LOCAL =
            "jdbc:sqlite:C:\\Users\\User\\git\\Ouyoukadai\\db\\kondate.db";

    // Render環境用のSQLite接続URL
    private static final String URL_RENDER =
            "jdbc:sqlite:/usr/local/tomcat/db/kondate.db";

    /**
     * データベース接続を取得する
     *
     * ローカル実行時：
     *   URL_LOCAL に接続
     *
     * Render実行時：
     *   URL_RENDER に接続
     */
    public static Connection getConnection()
            throws Exception {

        // SQLite JDBCドライバを読み込む
        Class.forName("org.sqlite.JDBC");

        // Render環境かどうかを判定
        String renderEnv = System.getenv("RENDER");

        // 実行環境に応じて接続先を切り替える
        if (renderEnv != null) {

            return DriverManager.getConnection(
                    URL_RENDER);

        } else {

            return DriverManager.getConnection(
                    URL_LOCAL);
        }
    }
}