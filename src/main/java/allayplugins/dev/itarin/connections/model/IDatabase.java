package allayplugins.dev.itarin.connections.model;

import java.sql.Connection;

public interface IDatabase {

    void openConnection();

    void closeConnection();

    void executeUpdate(String paramString, Object... paramVarArgs);

    void createTables();

    Connection getConnection();

}