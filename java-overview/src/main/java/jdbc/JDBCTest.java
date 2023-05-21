package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/25
 */
public class JDBCTest {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf8";
        String username = "root";
        String password = "123456";

        try (
            // 其实不需要手动加载 Class.forName("com.mysql.cj.jdbc.Driver")
            // 只需要引入对应数据库的 jar 包，DriverManager 会利用 SPI 机制自动加载
            Connection connection = DriverManager.getConnection(url, username, password);
        ) {

            // do something
            System.out.println(connection.isValid(5));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
