package sur.snapps.jetta.database.module;

import org.junit.runner.Description;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.core.TestResult;
import sur.snapps.jetta.core.config.JettaConfigurations;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.core.rules.JettaRuleModule;
import sur.snapps.jetta.database.counter.RecordCounter;
import sur.snapps.jetta.database.script.Script;
import sur.snapps.jetta.database.script.ScriptRunner;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

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
        injectRecordCounter(target);
        return true;
    }

    private void injectRecordCounter(Object target) {
        Set<Field> fields = ReflectionUtils.getAllFields(target.getClass());
        for (Field field : fields) {
            if (field.getType().equals(RecordCounter.class)) {
                ReflectionUtils.setFieldValue(target, field, new RecordCounter(getConnection(), configuration.databaseDialect()));
            }
        }
    }

    private void executeTestScript(Object target) {
        if (target.getClass().isAnnotationPresent(Script.class)) {
            Script script = target.getClass().getAnnotation(Script.class);
            executeScript(script);
        }
    }

    @Override
    public void quit(TestResult result) {
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
        switch (script.strategy()) {
            case CLEAN_FIRST:
                String scriptLocation = configuration.scriptLocation();
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + configuration.clearScript());
                ScriptRunner.executeScript(getConnection(), scriptLocation + "/" + script.value());
                break;
        }
    }
}
