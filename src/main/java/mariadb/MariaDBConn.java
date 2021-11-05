package mariadb;

import java.sql.*;

/**
 * @author richer.sun
 * @description 操作mariadb
 * @date 2021/9/28 6:56 下午
 */
public class MariaDBConn {
    public static void main(String[] args) {
// 数据库驱动
        String driver ="org.mariadb.jdbc.Driver";
        // url
        String url = "jdbc:mariadb://10.0.9.230:3306/test";
        // 用户名
        String user = "root";
        // 密码
        String pass = "yunzhihui123";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 注册数据库驱动
            Class.forName(driver);
            // 获取数据库连接
            conn = DriverManager.getConnection(url,user,pass);
            // 创建Statement对象
            stmt = conn.createStatement();
            // 要查询的语句
            String sql = "select id,`name`,age from stu";
            // 执行查询
            rs = stmt.executeQuery(sql);

            // 输出查询结果
            while(rs.next()) {
                System.out.println(rs.getString(1)
                        +rs.getString(2));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭结果
                if(rs!=null) {
                    rs.close();
                }
                // 关闭载体
                if(stmt!=null) {
                    stmt.close();
                }
                // 关闭连接
                if(conn!=null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
}
