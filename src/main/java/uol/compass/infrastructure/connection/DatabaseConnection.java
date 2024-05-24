package uol.compass.infrastructure.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import uol.compass.infrastructure.exception.ConnectionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DatabaseConnection {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        log.info("Iniciando tentativa de conexão com o banco de dados...");
        loadDatabaseProperties();
        config.setJdbcUrl(config.getJdbcUrl());
        config.setUsername(config.getUsername());
        config.setPassword(config.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
        log.info("Conexão com o banco de dados efetuada com sucesso.");
    }

    private DatabaseConnection() {}

    private static void loadDatabaseProperties() {
        Properties properties = new Properties();
        try(InputStream inputStream = DatabaseConnection.class.getResourceAsStream("/database.properties")) {
            if (inputStream == null) {
                log.error("Arquivo database.properties não encontrado no classpath");
                System.exit(1); // Encerra a execução caso não consiga encontrar o arquivo
            }
            properties.load(inputStream);
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));
        } catch (IOException e) {
            log.info("Erro ao carregar propriedades do banco de dados. Verifique se o arquivo 'database.properties' está configurado corretamente.");
            System.exit(1);
        }
    }

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
