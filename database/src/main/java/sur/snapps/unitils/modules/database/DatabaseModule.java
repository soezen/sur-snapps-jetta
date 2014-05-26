package sur.snapps.unitils.modules.database;

import org.unitils.core.Module;
import org.unitils.core.TestListener;
import org.unitils.core.UnitilsException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 15:53
 */
public class DatabaseModule implements Module {

    private DatabaseConfiguration configuration;
    private Connection connection;

    @Override
    public void init(Properties properties) {
        this.configuration = new DatabaseConfiguration(properties);
    }

    @Override
    public void afterInit() {
    }

    @Override
    public TestListener getTestListener() {
        return new DatabaseTestListener();
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
            throw new UnitilsException(e);
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

    protected class DatabaseTestListener extends TestListener {

        @Override
        public void beforeTestClass(Class<?> testClass) {
            if (testClass.isAnnotationPresent(Script.class)) {
                Script script = testClass.getAnnotation(Script.class);
                executeScript(script);
            }
            super.beforeTestClass(testClass);
        }

        @Override
        public void beforeTestSetUp(Object testObject, Method testMethod) {
            super.beforeTestSetUp(testObject, testMethod);
        }

        @Override
        public void afterTestTearDown(Object testObject, Method testMethod) {
            super.afterTestTearDown(testObject, testMethod);
        }
    }
}
