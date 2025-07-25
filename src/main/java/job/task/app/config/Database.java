package job.task.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database
{

    private final DataSource dataSource;

    public Database(String url, String username, String password)
    {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }
}
