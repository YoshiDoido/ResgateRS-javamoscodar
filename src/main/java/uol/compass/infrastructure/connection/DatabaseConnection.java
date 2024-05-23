package uol.compass.infrastructure.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import uol.compass.infrastructure.exception.ConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DatabaseConnection {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    private static final String URL = "jdbc:mysql://localhost:3306/resgate_rs?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
<<<<<<< HEAD
    private static final String PASSWORD = "";
=======
    private static final String PASSWORD = "1234567";
>>>>>>> 2e472296ab75f94537dd83d74a06dac67bf11232

    static {
        log.info("Iniciando tentativa de conexão com o banco de dados...");
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
        log.info("Conexão com o banco de dados efetuada com sucesso.");
    }

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new ConnectionException("Erro ao estabelecer conexão com o banco de dados", e);
        }
    }

    public static void testConnection() {
        try(Connection connection = ds.getConnection()) {
        } catch (SQLException e) {
            throw new ConnectionException("Erro ao estabelecer conexão com o banco de dados", e);
        }
    }
}
