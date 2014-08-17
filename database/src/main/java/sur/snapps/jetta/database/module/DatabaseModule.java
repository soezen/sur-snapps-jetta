package sur.snapps.jetta.database.module;

import org.unitils.util.ReflectionUtils;
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

    private DatabaseConfiguration configuration;
    private Connection connection;

    @Override
    public void init(Object target, String testName) {
        executeTestScript(target);
        injectRecordCounter(target);
    }

    private void injectRecordCounter(Object target) {
        Set<Field> fields = ReflectionUtils.getAllFields(target.getClass());
        for (Field field : fields) {
            if (field.getType().equals(RecordCounter.class)) {
                ReflectionUtils.setFieldValue(target, field, new RecordCounter(connection, configuration.databaseDialect()));
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
    public void quit(boolean success) {
        try {
            connection.close();
        } catch (SQLException ignored) {
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
