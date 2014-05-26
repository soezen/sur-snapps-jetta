package sur.snapps.unitils.modules.database;

import java.util.Properties;

import static org.unitils.util.PropertyUtils.getString;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 18:57
 */
public class DatabaseConfiguration {

    private static final String PREFIX = "sur.snapps.module.database.";

    private static final String SCRIPT_LOCATION = PREFIX + "script.location";
    private static final String CLEAR_SCRIPT = PREFIX + "script.clear";

    private static final String DATASOURCE_DRIVER = PREFIX + "datasource.driver";
    private static final String DATASOURCE_URL = PREFIX + "datasource.url";
    private static final String DATASOURCE_USERNAME = PREFIX + "datasource.username";
    private static final String DATASOURCE_PASSWORD = PREFIX + "datasource.password";


    private String scriptLocation;
    private String clearScript;
    private String dataSourceDriver;
    private String dataSourceUrl;
    private String dataSourceUsername;
    private String dataSourcePassword;


    public DatabaseConfiguration(Properties properties) {
        clearScript = getString(CLEAR_SCRIPT, properties);
        scriptLocation = getString(SCRIPT_LOCATION, properties);
        dataSourceDriver = getString(DATASOURCE_DRIVER, properties);
        dataSourceUrl = getString(DATASOURCE_URL, properties);
        dataSourceUsername = getString(DATASOURCE_USERNAME, properties);
        dataSourcePassword = getString(DATASOURCE_PASSWORD, properties);
    }

    public DataSource dataSource() {
        return new DataSource();
    }

    public String scriptLocation() {
        return scriptLocation;
    }

    public String clearScript() {
        return clearScript;
    }

    public class DataSource {

        public String driver() {
            return dataSourceDriver;
        }

        public String url() {
            return dataSourceUrl;
        }

        public String username() {
            return dataSourceUsername;
        }

        public String password() {
            return dataSourcePassword;
        }
    }
}
