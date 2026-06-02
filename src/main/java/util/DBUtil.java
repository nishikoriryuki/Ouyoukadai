package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static final String URL =
            "jdbc:sqlite:/usr/local/tomcat/db/kondate.db";

    public static Connection getConnection()
            throws Exception {

        Class.forName("org.sqlite.JDBC");

        return DriverManager.getConnection(URL);
    }
}