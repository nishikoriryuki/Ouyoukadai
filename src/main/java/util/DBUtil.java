package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {



    private static final String URL_LOCAL = "jdbc:sqlite:C:/Users/User/git/Ouyoukadai/db/kondate.db";


    private static final String URL_RENDER =
            "jdbc:sqlite:/usr/local/tomcat/db/kondate.db";

   
    public static Connection getConnection()
            throws Exception {

        Class.forName("org.sqlite.JDBC");

        String renderEnv = System.getenv("RENDER");

        if (renderEnv != null) {
            return DriverManager.getConnection(URL_RENDER);
        } else {
            return DriverManager.getConnection(URL_LOCAL);
        }
    }
}