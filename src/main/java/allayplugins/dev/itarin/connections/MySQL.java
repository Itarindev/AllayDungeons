package allayplugins.dev.itarin.connections;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.connections.model.IDatabase;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL implements IDatabase {

    @Getter
    private Connection connection;

    private final FileConfiguration config = AllayDungeons.getInstance().getConfig();

    public MySQL() {
        openConnection();
        createTables();
    }

    public void openConnection() {
        String host = config.getString("MySQL.Host");
        String user = config.getString("MySQL.User");
        String password = config.getString("MySQL.Password");
        String db = config.getString("MySQL.Database");
        String url = "jdbc:mysql://" + host + "/" + db + "?autoReconnect=true";
        try {
            connection = DriverManager.getConnection(url, user, password);
            Bukkit.getConsoleSender().sendMessage("§6[AllayDungeons] §aA conexão com §eMySQL §afoi iniciado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection == null)
            return;
        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage("§6[AllayDungeons] §aA conexão com §eMySQL §afoi fechada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeUpdate(String query, Object... params) {
        try (val ps = connection.prepareStatement(query)) {
            if (params != null && params.length > 0)
                for (int index = 0; index < params.length; index++)
                    ps.setObject(index + 1, params[index]);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `allaydungeons_users_rewards` (user_uuid VARCHAR(36), user_name VARCHAR (24), dungeon_id TEXT NOT NULL, rewards LONGTEXT NOT NULL)");
    }
}