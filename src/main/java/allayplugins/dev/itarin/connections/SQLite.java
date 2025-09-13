package allayplugins.dev.itarin.connections;

import allayplugins.dev.itarin.connections.model.IDatabase;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements IDatabase {

    @Getter
    private Connection connection;

    public SQLite() {
        openConnection();
        createTables();
    }

    public void openConnection() {
        val file = new File("plugins/AllayDungeons/cache/database.db");
        String url = "jdbc:sqlite:" + file;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);

            Bukkit.getConsoleSender().sendMessage("§6[AllayDungeons] §aA conexão com §eSQLite §afoi iniciada com sucesso.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection == null)
            return;
        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage("§6[AllayDungeons] §aA conexão com §eSQLite §afoi fechada com sucesso.");

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