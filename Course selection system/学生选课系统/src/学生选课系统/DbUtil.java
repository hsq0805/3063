package 学生选课系统;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbUtil {
    private String dbUrl = "jdbc:mysql://localhost:3306/CourseSelection";
    private String dbUserName = "root";
    private String dbPassword = "123456";
    private String jdbcName = "com.mysql.cj.jdbc.Driver";

    public DbUtil() {
    }

    public Connection getCon() throws Exception {
        Class.forName(this.jdbcName);
        Connection con = DriverManager.getConnection(this.dbUrl, this.dbUserName, this.dbPassword);
        return con;
    }

    public void closeCon(Connection con) throws Exception {
        if (con != null) {
            con.close();
        }

    }

    public static void main(String[] args) {
        DbUtil dbUtil = new DbUtil();

        try {
            dbUtil.getCon();
            System.out.println("数据库连接成功!");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}

