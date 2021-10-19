package io.github.escapehonbab.jpa;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.Transaction;
import io.ebean.annotation.TxIsolation;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.dbmigration.DbMigration;
import io.github.escapehonbab.jpa.objects.Sex;
import io.github.escapehonbab.jpa.objects.User;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    private static DatabaseHandler instance;
    private final String USERNAME = "root";
    private final String PASSWORD = "1234";
    private final String URL = "jdbc:mariadb://localhost:3306/test?useLegacyDatetimeCode=false";
    @Getter
    private Database database;

    public static DatabaseHandler getInstance() {
        if (instance == null)
            instance = new DatabaseHandler();
        return instance;
    }

    public void initializeDatabase() {
        DataSourceConfig sourceConfig = new DataSourceConfig();
        sourceConfig.setDriver("org.mariadb.jdbc.Driver");
        sourceConfig.setUsername(USERNAME);
        sourceConfig.setPassword(PASSWORD);
        sourceConfig.setUrl(URL);
        sourceConfig.setIsolationLevel(TxIsolation.SERIALIZABLE.getLevel());

        DatabaseConfig config = new DatabaseConfig();
        config.setName("MariaDB");
        config.setRegister(false);
        config.setDataSourceConfig(sourceConfig);

        config.addClass(User.class);
        config.addClass(Sex.class);

        database = DatabaseFactory.create(config);

        DbMigration ddlGenerator = DbMigration.create();
        ddlGenerator.setServer(database);

        File folder = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "ddl");

        if (!folder.exists())
            folder.mkdir();
        ddlGenerator.setPathToResources(folder.getPath());

        File sqlLoc = new File(folder, "dbinit\\1.0__initial.sql");
        for (Class<?> cls : config.getClasses()) {
            try {
                database.find(cls).findCount();
            } catch (Exception ex) {
                try (Transaction transaction = database.beginTransaction()) {
                    Connection con = transaction.getConnection();
                    String[] scripts = readLine(sqlLoc).split(";");
                    for (String s : scripts) {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(s);
                        if (rs != null)
                            rs.close();
                    }
                    break;
                } catch (SQLException ex2) {
                    ex2.printStackTrace();
                } finally {
                    database.endTransaction();
                }
            }
        }

    }

    private String readLine(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                if (!buffer.startsWith("--"))
                    builder.append(buffer);
            }
            return builder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
