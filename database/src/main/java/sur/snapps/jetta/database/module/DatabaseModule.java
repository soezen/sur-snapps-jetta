package sur.snapps.jetta.database.module;

import org.junit.runner.Description;
import sur.snapps.jetta.core.TestResult;
import sur.snapps.jetta.core.config.JettaConfigurations;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.core.rules.JettaRuleModule;
import sur.snapps.jetta.database.counter.RecordCounter;
import sur.snapps.jetta.database.script.Script;
import sur.snapps.jetta.database.script.ScriptRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.unitils.util.AnnotationUtils.getClassLevelAnnotation;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 15:53
 */
public class DatabaseModule extends JettaRuleModule {

    private DatabaseConfiguration configuration = JettaConfigurations.get(DatabaseConfiguration.class);
    private Connection connection;

    private static DatabaseModule instance;

    private DatabaseModule() { }

    public static DatabaseModule getInstance() {
        if (instance == null) {
            instance = new DatabaseModule();
        }
        return instance;
    }

    @Override
    public boolean init(Object target, Description description) {
        JettaLogger.info(this.getClass(), "ACTIVATED");
        executeTestScript(target);
        return true;
    }

    public RecordCounter recordCounter() {
        return new RecordCounter(getConnection(), configuration.databaseDialect());
    }

    private void executeTestScript(Object target) {
        Script scriptAnnotation = getClassLevelAnnotation(Script.class, target.getClass());
        // TODO execute super script also
        // TODO allow config to say that script on class only executed once for all tests within class
        if (scriptAnnotation != null) {
            executeScript(scriptAnnotation);
        }
    }

    @Override
    public void quit(TestResult result) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }


    private Connection getConnection() {
        initializeConnection();
        return connection;
    }

    private void initializeConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                DatabaseConfiguration.DataSource dataSource = configuration.dataSource();
                Class.forName(dataSource.driver());
                connection = DriverManager.getConnection(
                        dataSource.url(),
                        dataSource.username(),
                        dataSource.password());
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void executeScript(Script script) {
        String scriptLocation = configuration.scriptLocation();
        switch (script.strategy()) {
            case CLEAN_FIRST:
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + configuration.clearScript());
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + script.value());
                break;
            case CREATE_FIRST:
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + configuration.createScript());
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + script.value());
                break;
        }
    }
}
