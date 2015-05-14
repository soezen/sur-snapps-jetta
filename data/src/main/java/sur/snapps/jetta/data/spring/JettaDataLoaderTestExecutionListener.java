package sur.snapps.jetta.data.spring;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.unitils.util.ReflectionUtils;
import sur.snapps.jetta.core.config.JettaConfigurations;
import sur.snapps.jetta.core.exception.ExceptionFactory;
import sur.snapps.jetta.data.DataSet;
import sur.snapps.jetta.data.dbunit.JettaDataSet;
import sur.snapps.jetta.data.module.DataConfiguration;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.RecordCounter;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Set;

import static org.dbunit.operation.DatabaseOperation.CLEAN_INSERT;
import static org.unitils.util.AnnotationUtils.getClassLevelAnnotation;
import static org.unitils.util.ReflectionUtils.getFieldsOfType;
import static sur.snapps.jetta.core.exception.ErrorType.MISSING_PROPERTY;
import static sur.snapps.jetta.data.module.DataConfiguration.PROPERTY_DATA_SOURCE_BEAN_NAME;

/**
 * @author sur
 * @since 06/03/2015
 */
public class JettaDataLoaderTestExecutionListener extends AbstractTestExecutionListener {

    private DataConfiguration configuration;
    private ApplicationContext appContext;
    private TestContext testContext;
    private IDatabaseConnection connection;

    public JettaDataLoaderTestExecutionListener() {
        configuration = JettaConfigurations.get(DataConfiguration.class);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {

        DatabaseOperation operation = CLEAN_INSERT;

        DataSet dataAnnotation = testContext.getTestMethod().getAnnotation(DataSet.class);
        DataSet classDataAnnotation = getClassLevelAnnotation(DataSet.class, testContext.getTestClass());
        if (dataAnnotation != null) {
            IDataSet dataSet = new JettaDataSet(dataAnnotation);
            operation.execute(connection, dataSet);
        } else if (classDataAnnotation != null) {
            IDataSet dataSet = new JettaDataSet(classDataAnnotation);
            operation.execute(connection, dataSet);
        }

    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        this.testContext = testContext;
        this.appContext = testContext.getApplicationContext();

        checkConfiguration();
        prepareDatabaseConnection();
        injectRecordCounters();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    private void injectRecordCounters() throws Exception {
        Set<Field> recordCounters = getFieldsOfType(testContext.getTestClass(), RecordCounter.class, false);
        for (Field field : recordCounters) {
            ReflectionUtils.setFieldValue(testContext.getTestInstance(), field, new RecordCounter(connection.getConnection(), DatabaseDialect.HSQLDB));
        }
    }

    private void prepareDatabaseConnection() {
        DataSource dataSource = appContext.getBean(configuration.dataSourceBeanName(), DataSource.class);
        connection = newConnection(dataSource);
    }

    private IDatabaseConnection newConnection(DataSource dataSource) {
        try {
            if (!(dataSource instanceof TransactionAwareDataSourceProxy)) {
                dataSource = new TransactionAwareDataSourceProxy(dataSource);
            }
            return new DatabaseDataSourceConnection(dataSource, null, null, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfiguration() {
        if (!appContext.containsBean(configuration.dataSourceBeanName())) {
            throw ExceptionFactory.create(MISSING_PROPERTY, PROPERTY_DATA_SOURCE_BEAN_NAME);
        }
    }
}
