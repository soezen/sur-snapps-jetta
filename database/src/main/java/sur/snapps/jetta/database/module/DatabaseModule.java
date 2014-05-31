package sur.snapps.jetta.database.module;

import sur.snapps.jetta.core.rules.JettaRuleModule;
import sur.snapps.jetta.database.script.Script;
import sur.snapps.jetta.database.script.ScriptRunner;
import sur.snapps.jetta.database.module.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 15:53
 */
public class DatabaseModule extends JettaRuleModule {

    private DatabaseConfiguration configuration;
    private Connection connection;

    @Override
    public void init(Object target, String testName) {

        if (target.getClass().isAnnotationPresent(Script.class)) {
            Script script = target.getClass().getAnnotation(Script.class);
            executeScript(script);
        }
    }

    @Override
    public void quit() {
        try {
            connection.close();
        } catch (SQLException e) {

        }
    }


    private Connection getConnection() {
        if (connection == null) {
            initializeConnection();
        }
        return connection;
    }

    private void initializeConnection() {
        try {
            DatabaseConfiguration.DataSource dataSource = configuration.dataSource();
            Class.forName(dataSource.driver());
            connection = DriverManager.getConnection(
                    dataSource.url(),
                    dataSource.username(),
                    dataSource.password());
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void executeScript(Script script) {
        switch (script.strategy()) {
            case CLEAN_FIRST:
                String scriptLocation = configuration.scriptLocation();
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + configuration.clearScript());
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + script.value());
                break;
        }
    }
}
